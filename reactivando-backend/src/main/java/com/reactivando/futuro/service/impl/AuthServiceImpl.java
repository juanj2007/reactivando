package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.UsuarioRequestDTO;
import com.reactivando.futuro.dto.UsuarioResponseDTO;
import com.reactivando.futuro.dto.auth.AuthResponseDTO;
import com.reactivando.futuro.dto.auth.LoginRequestDTO;
import com.reactivando.futuro.dto.auth.RegisterRequestDTO;
import com.reactivando.futuro.entity.Rol;
import com.reactivando.futuro.exception.BadRequestException;
import com.reactivando.futuro.security.JwtTokenProvider;
import com.reactivando.futuro.service.AuthService;
import com.reactivando.futuro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioService usuarioService;
    private final com.reactivando.futuro.service.AuditoriaService auditoriaService;

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getCorreo(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generarToken(authentication);
        UsuarioResponseDTO usuario = usuarioService.obtenerPorCorreo(loginDTO.getCorreo());

        auditoriaService.registrarEvento(
                loginDTO.getCorreo(),
                "INICIO_SESION",
                "Inicio de sesión exitoso para el usuario " + usuario.getCorreo() + " (Rol: " + usuario.getRol() + ")",
                "127.0.0.1"
        );

        return AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .usuario(usuario)
                .build();
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerDTO) {
        // Validaciones estrictas de límites de caracteres por rol
        validarLimitesCaracteres(registerDTO);

        UsuarioRequestDTO usuarioRequest = UsuarioRequestDTO.builder()
                .nombre(registerDTO.getNombre())
                .apellido(registerDTO.getApellido())
                .correo(registerDTO.getCorreo())
                .password(registerDTO.getPassword())
                .telefono(registerDTO.getTelefono())
                .rol(registerDTO.getRol())
                .build();

        UsuarioResponseDTO usuarioCreado = usuarioService.registrarUsuario(usuarioRequest);

        auditoriaService.registrarEvento(
                registerDTO.getCorreo(),
                "REGISTRO_USUARIO",
                "Registro de nuevo usuario con rol " + registerDTO.getRol() + " (" + registerDTO.getNombre() + " " + registerDTO.getApellido() + ")",
                "127.0.0.1"
        );

        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
                .correo(registerDTO.getCorreo())
                .password(registerDTO.getPassword())
                .build();

        return login(loginRequest);
    }

    private void validarLimitesCaracteres(RegisterRequestDTO dto) {
        if (dto.getRol() == Rol.CANDIDATO) {
            if (dto.getNombre() == null || dto.getNombre().trim().length() < 2 || dto.getNombre().trim().length() > 50) {
                throw new BadRequestException("El nombre del candidato debe tener entre 2 y 50 caracteres.");
            }
            if (dto.getApellido() == null || dto.getApellido().trim().length() < 2 || dto.getApellido().trim().length() > 50) {
                throw new BadRequestException("El apellido del candidato debe tener entre 2 y 50 caracteres.");
            }
        } else if (dto.getRol() == Rol.EMPRESA) {
            if (dto.getNombre() == null || dto.getNombre().trim().length() < 3 || dto.getNombre().trim().length() > 100) {
                throw new BadRequestException("El nombre de la empresa / razón social debe tener entre 3 y 100 caracteres.");
            }
            if (dto.getApellido() == null || dto.getApellido().trim().length() < 3 || dto.getApellido().trim().length() > 80) {
                throw new BadRequestException("El nombre del representante legal debe tener entre 3 y 80 caracteres.");
            }
        }

        if (dto.getCorreo() == null || dto.getCorreo().trim().length() < 5 || dto.getCorreo().trim().length() > 80) {
            throw new BadRequestException("El correo electrónico debe tener entre 5 y 80 caracteres.");
        }

        if (dto.getTelefono() == null || dto.getTelefono().trim().length() < 7 || dto.getTelefono().trim().length() > 15) {
            throw new BadRequestException("El teléfono debe tener entre 7 y 15 caracteres/dígitos.");
        }

        if (dto.getPassword() == null || dto.getPassword().length() < 6 || dto.getPassword().length() > 30) {
            throw new BadRequestException("La contraseña debe tener entre 6 y 30 caracteres.");
        }
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioActual(String correo) {
        return usuarioService.obtenerPorCorreo(correo);
    }
}
