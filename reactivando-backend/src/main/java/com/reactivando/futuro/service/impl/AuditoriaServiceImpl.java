package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.auditoria.AuditoriaResponseDTO;
import com.reactivando.futuro.entity.Auditoria;
import com.reactivando.futuro.entity.Usuario;
import com.reactivando.futuro.mapper.AuditoriaMapper;
import com.reactivando.futuro.repository.AuditoriaRepository;
import com.reactivando.futuro.repository.UsuarioRepository;
import com.reactivando.futuro.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    @Transactional
    public void registrarEvento(String correoUsuario, String accion, String descripcion, String ip) {
        Usuario usuario = null;
        if (correoUsuario != null && !correoUsuario.isBlank()) {
            usuario = usuarioRepository.findByCorreo(correoUsuario).orElse(null);
        }

        Auditoria auditoria = Auditoria.builder()
                .usuario(usuario)
                .accion(accion)
                .descripcion(descripcion)
                .ip(ip != null ? ip : "127.0.0.1")
                .build();

        auditoriaRepository.save(auditoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditoriaResponseDTO> listarEventos(Pageable pageable) {
        return auditoriaRepository.findAllByOrderByFechaDesc(pageable)
                .map(auditoriaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditoriaResponseDTO> listarEventosPorUsuario(Long usuarioId, Pageable pageable) {
        return auditoriaRepository.findByUsuarioId(usuarioId, pageable)
                .map(auditoriaMapper::toResponseDTO);
    }
}
