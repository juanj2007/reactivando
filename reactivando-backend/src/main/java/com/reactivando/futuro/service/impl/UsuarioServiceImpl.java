package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.UsuarioRequestDTO;
import com.reactivando.futuro.dto.UsuarioResponseDTO;
import com.reactivando.futuro.entity.Candidato;
import com.reactivando.futuro.entity.Empresa;
import com.reactivando.futuro.entity.Rol;
import com.reactivando.futuro.entity.Usuario;
import com.reactivando.futuro.exception.DuplicateResourceException;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.UsuarioMapper;
import com.reactivando.futuro.repository.CandidatoRepository;
import com.reactivando.futuro.repository.EmpresaRepository;
import com.reactivando.futuro.repository.UsuarioRepository;
import com.reactivando.futuro.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CandidatoRepository candidatoRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO) {
        if (usuarioRepository.existsByCorreo(requestDTO.getCorreo())) {
            throw new DuplicateResourceException("El correo ya se encuentra registrado: " + requestDTO.getCorreo());
        }

        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        usuario.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Crear perfil asociado automáticamente según el rol
        if (usuarioGuardado.getRol() == Rol.CANDIDATO) {
            Candidato candidato = Candidato.builder()
                    .usuario(usuarioGuardado)
                    .build();
            candidatoRepository.save(candidato);
        } else if (usuarioGuardado.getRol() == Rol.EMPRESA) {
            Empresa empresa = Empresa.builder()
                    .usuario(usuarioGuardado)
                    .nombreEmpresa(usuarioGuardado.getNombre() + " " + usuarioGuardado.getApellido())
                    .nit("NIT-" + usuarioGuardado.getId())
                    .build();
            empresaRepository.save(empresa);
        }

        return usuarioMapper.toResponseDTO(usuarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (!usuario.getCorreo().equalsIgnoreCase(requestDTO.getCorreo()) && usuarioRepository.existsByCorreo(requestDTO.getCorreo())) {
            throw new DuplicateResourceException("El correo ya está en uso por otro usuario: " + requestDTO.getCorreo());
        }

        usuario.setNombre(requestDTO.getNombre());
        usuario.setApellido(requestDTO.getApellido());
        usuario.setCorreo(requestDTO.getCorreo());
        usuario.setTelefono(requestDTO.getTelefono());

        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }

    @Override
    @Transactional
    public void cambiarEstadoUsuario(Long id, boolean estado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setEstado(estado);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
