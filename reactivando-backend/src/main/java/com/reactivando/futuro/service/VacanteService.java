package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.vacante.VacanteRequestDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.EstadoVacante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface VacanteService {
    VacanteResponseDTO crearVacante(String correoUsuario, VacanteRequestDTO requestDTO);
    VacanteResponseDTO obtenerPorId(Long id);
    Page<VacanteResponseDTO> listarVacantes(Pageable pageable);
    Page<VacanteResponseDTO> buscarVacantes(
            String titulo,
            String ciudad,
            String tipoContrato,
            String nivelEstudio,
            BigDecimal salarioMin,
            BigDecimal salarioMax,
            EstadoVacante estado,
            Pageable pageable
    );
    Page<VacanteResponseDTO> listarPorEmpresa(Long empresaId, Pageable pageable);
    List<VacanteResponseDTO> listarTodasPorEmpresa(Long empresaId);
    VacanteResponseDTO actualizarVacante(Long id, String correoUsuario, VacanteRequestDTO requestDTO);
    VacanteResponseDTO cambiarEstado(Long id, String correoUsuario, EstadoVacante nuevoEstado);
    void eliminarVacante(Long id, String correoUsuario);
}
