package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.CandidatoRequestDTO;
import com.reactivando.futuro.dto.CandidatoResponseDTO;
import com.reactivando.futuro.entity.Candidato;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.CandidatoMapper;
import com.reactivando.futuro.repository.CandidatoRepository;
import com.reactivando.futuro.service.CandidatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatoServiceImpl implements CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final CandidatoMapper candidatoMapper;

    @Override
    @Transactional(readOnly = true)
    public CandidatoResponseDTO obtenerPorId(Long id) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con ID: " + id));
        return candidatoMapper.toResponseDTO(candidato);
    }

    @Override
    @Transactional(readOnly = true)
    public CandidatoResponseDTO obtenerPorUsuarioId(Long usuarioId) {
        Candidato candidato = candidatoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado para el usuario con ID: " + usuarioId));
        return candidatoMapper.toResponseDTO(candidato);
    }

    @Override
    @Transactional(readOnly = true)
    public CandidatoResponseDTO obtenerPorCorreo(String correo) {
        Candidato candidato = candidatoRepository.findByUsuarioCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado para el correo: " + correo));
        return candidatoMapper.toResponseDTO(candidato);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CandidatoResponseDTO> obtenerTodos() {
        return candidatoRepository.findAll().stream()
                .map(candidatoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CandidatoResponseDTO actualizarPerfil(Long usuarioId, CandidatoRequestDTO requestDTO) {
        Candidato candidato = candidatoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de candidato no encontrado para el usuario con ID: " + usuarioId));

        if (requestDTO.getFechaNacimiento() != null) candidato.setFechaNacimiento(requestDTO.getFechaNacimiento());
        if (requestDTO.getEdad() != null) candidato.setEdad(requestDTO.getEdad());
        if (requestDTO.getNivelEstudio() != null) candidato.setNivelEstudio(requestDTO.getNivelEstudio());
        if (requestDTO.getOcupacion() != null) candidato.setOcupacion(requestDTO.getOcupacion());
        if (requestDTO.getDireccion() != null) candidato.setDireccion(requestDTO.getDireccion());
        if (requestDTO.getCiudad() != null) candidato.setCiudad(requestDTO.getCiudad());
        if (requestDTO.getDescripcion() != null) candidato.setDescripcion(requestDTO.getDescripcion());
        if (requestDTO.getExperiencia() != null) candidato.setExperiencia(requestDTO.getExperiencia());
        if (requestDTO.getHabilidades() != null) candidato.setHabilidades(requestDTO.getHabilidades());
        if (requestDTO.getHojaDeVida() != null) candidato.setHojaDeVida(requestDTO.getHojaDeVida());
        if (requestDTO.getFoto() != null) candidato.setFoto(requestDTO.getFoto());

        Candidato actualizado = candidatoRepository.save(candidato);
        return candidatoMapper.toResponseDTO(actualizado);
    }
}
