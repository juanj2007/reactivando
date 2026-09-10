package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.EmpresaRequestDTO;
import com.reactivando.futuro.dto.EmpresaResponseDTO;
import com.reactivando.futuro.entity.Empresa;
import com.reactivando.futuro.exception.DuplicateResourceException;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.EmpresaMapper;
import com.reactivando.futuro.repository.EmpresaRepository;
import com.reactivando.futuro.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EmpresaMapper empresaMapper;
    private final com.reactivando.futuro.external.GoogleMapsService googleMapsService;

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponseDTO obtenerPorId(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + id));
        return empresaMapper.toResponseDTO(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public com.reactivando.futuro.dto.empresa.UbicacionEmpresaDTO obtenerUbicacion(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada con ID: " + empresaId));
        return googleMapsService.obtenerUbicacionEmpresa(empresa);
    }

    @Override
    @Transactional
    public EmpresaResponseDTO actualizarCoordenadas(Long usuarioId, Double latitud, Double longitud) {
        Empresa empresa = empresaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado para el usuario con ID: " + usuarioId));

        empresa.setLatitud(latitud);
        empresa.setLongitud(longitud);

        Empresa actualizada = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(actualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponseDTO obtenerPorUsuarioId(Long usuarioId) {
        Empresa empresa = empresaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada para el usuario con ID: " + usuarioId));
        return empresaMapper.toResponseDTO(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponseDTO obtenerPorCorreo(String correo) {
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada para el correo: " + correo));
        return empresaMapper.toResponseDTO(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponseDTO> obtenerTodas() {
        return empresaRepository.findAll().stream()
                .map(empresaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmpresaResponseDTO actualizarPerfil(Long usuarioId, EmpresaRequestDTO requestDTO) {
        Empresa empresa = empresaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado para el usuario con ID: " + usuarioId));

        if (!empresa.getNit().equalsIgnoreCase(requestDTO.getNit()) && empresaRepository.existsByNit(requestDTO.getNit())) {
            throw new DuplicateResourceException("El NIT " + requestDTO.getNit() + " ya se encuentra registrado por otra empresa");
        }

        if (requestDTO.getNombreEmpresa() != null) empresa.setNombreEmpresa(requestDTO.getNombreEmpresa());
        if (requestDTO.getNit() != null) empresa.setNit(requestDTO.getNit());
        if (requestDTO.getDescripcion() != null) empresa.setDescripcion(requestDTO.getDescripcion());
        if (requestDTO.getDireccion() != null) empresa.setDireccion(requestDTO.getDireccion());
        if (requestDTO.getCiudad() != null) empresa.setCiudad(requestDTO.getCiudad());
        if (requestDTO.getTelefono() != null) empresa.setTelefono(requestDTO.getTelefono());
        if (requestDTO.getCorreoCorporativo() != null) empresa.setCorreoCorporativo(requestDTO.getCorreoCorporativo());
        if (requestDTO.getNumeroEmpleados() != null) empresa.setNumeroEmpleados(requestDTO.getNumeroEmpleados());
        if (requestDTO.getSitioWeb() != null) empresa.setSitioWeb(requestDTO.getSitioWeb());
        if (requestDTO.getLatitud() != null) empresa.setLatitud(requestDTO.getLatitud());
        if (requestDTO.getLongitud() != null) empresa.setLongitud(requestDTO.getLongitud());

        Empresa actualizada = empresaRepository.save(empresa);
        return empresaMapper.toResponseDTO(actualizada);
    }
}
