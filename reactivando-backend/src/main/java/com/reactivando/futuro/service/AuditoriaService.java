package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.auditoria.AuditoriaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditoriaService {
    void registrarEvento(String correoUsuario, String accion, String descripcion, String ip);
    Page<AuditoriaResponseDTO> listarEventos(Pageable pageable);
    Page<AuditoriaResponseDTO> listarEventosPorUsuario(Long usuarioId, Pageable pageable);
}
