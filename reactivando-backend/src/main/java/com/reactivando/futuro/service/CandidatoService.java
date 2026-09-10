package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.CandidatoRequestDTO;
import com.reactivando.futuro.dto.CandidatoResponseDTO;

import java.util.List;

public interface CandidatoService {
    CandidatoResponseDTO obtenerPorId(Long id);
    CandidatoResponseDTO obtenerPorUsuarioId(Long usuarioId);
    CandidatoResponseDTO obtenerPorCorreo(String correo);
    List<CandidatoResponseDTO> obtenerTodos();
    CandidatoResponseDTO actualizarPerfil(Long usuarioId, CandidatoRequestDTO requestDTO);
}
