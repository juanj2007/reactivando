package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.UsuarioRequestDTO;
import com.reactivando.futuro.dto.UsuarioResponseDTO;
import com.reactivando.futuro.dto.auth.AuthResponseDTO;
import com.reactivando.futuro.dto.auth.LoginRequestDTO;
import com.reactivando.futuro.dto.auth.RegisterRequestDTO;
import com.reactivando.futuro.entity.Rol;
import com.reactivando.futuro.security.JwtTokenProvider;
import com.reactivando.futuro.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequestDTO registerDTO;
    private LoginRequestDTO loginDTO;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    void setUp() {
        registerDTO = RegisterRequestDTO.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan.perez@email.com")
                .password("password123")
                .telefono("3001234567")
                .rol(Rol.CANDIDATO)
                .build();

        loginDTO = LoginRequestDTO.builder()
                .correo("juan.perez@email.com")
                .password("password123")
                .build();

        usuarioResponseDTO = UsuarioResponseDTO.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan.perez@email.com")
                .rol(Rol.CANDIDATO)
                .estado(true)
                .build();
    }

    @Test
    @DisplayName("Debe autenticar correctamente a un usuario y retornar token JWT")
    void loginExitoso() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generarToken(any(Authentication.class)))
                .thenReturn("mocked-jwt-token");
        when(usuarioService.obtenerPorCorreo("juan.perez@email.com"))
                .thenReturn(usuarioResponseDTO);

        AuthResponseDTO response = authService.login(loginDTO);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertEquals("juan.perez@email.com", response.getUsuario().getCorreo());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generarToken(authentication);
        verify(auditoriaService).registrarEvento(eq("juan.perez@email.com"), eq("INICIO_SESION"), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe lanzar BadCredentialsException si las credenciales son incorrectas")
    void loginCredencialesIncorrectas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales inválidas"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginDTO));

        verify(tokenProvider, never()).generarToken(any());
    }

    @Test
    @DisplayName("Debe registrar exitosamente un nuevo candidato")
    void registerExitoso() {
        when(usuarioService.registrarUsuario(any(UsuarioRequestDTO.class)))
                .thenReturn(usuarioResponseDTO);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generarToken(any(Authentication.class)))
                .thenReturn("mocked-jwt-token");
        when(usuarioService.obtenerPorCorreo("juan.perez@email.com"))
                .thenReturn(usuarioResponseDTO);

        AuthResponseDTO response = authService.register(registerDTO);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(usuarioService).registrarUsuario(any(UsuarioRequestDTO.class));
    }
}
