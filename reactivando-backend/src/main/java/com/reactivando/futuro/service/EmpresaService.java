package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.EmpresaRequestDTO;
import com.reactivando.futuro.dto.EmpresaResponseDTO;

import java.util.List;

public interface EmpresaService {
    EmpresaResponseDTO obtenerPorId(Long id);
    EmpresaResponseDTO obtenerPorUsuarioId(Long usuarioId);
    EmpresaResponseDTO obtenerPorCorreo(String correo);
    List<EmpresaResponseDTO> obtenerTodas();
    EmpresaResponseDTO actualizarPerfil(Long usuarioId, EmpresaRequestDTO requestDTO);
    com.reactivando.futuro.dto.empresa.UbicacionEmpresaDTO obtenerUbicacion(Long empresaId);
    EmpresaResponseDTO actualizarCoordenadas(Long usuarioId, Double latitud, Double longitud);
}
