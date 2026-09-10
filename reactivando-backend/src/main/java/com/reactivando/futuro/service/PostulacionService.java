package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.postulacion.CambiarEstadoPostulacionDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionRequestDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionResponseDTO;
import com.reactivando.futuro.entity.EstadoPostulacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostulacionService {
    PostulacionResponseDTO postularse(String correoCandidato, PostulacionRequestDTO requestDTO);
    PostulacionResponseDTO obtenerPorId(Long id, String correoUsuario);
    Page<PostulacionResponseDTO> listarMisPostulaciones(String correoCandidato, Pageable pageable);
    Page<PostulacionResponseDTO> listarPostulacionesPorVacante(Long vacanteId, String correoEmpresa, Pageable pageable);
    Page<PostulacionResponseDTO> listarPostulacionesPorEmpresa(String correoEmpresa, Pageable pageable);
    PostulacionResponseDTO cambiarEstadoPostulacion(Long id, String correoEmpresa, CambiarEstadoPostulacionDTO dto);
    void cancelarPostulacion(Long id, String correoCandidato);
}
