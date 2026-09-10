package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.vacante.VacanteRequestDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.Empresa;
import com.reactivando.futuro.entity.EstadoVacante;
import com.reactivando.futuro.entity.Rol;
import com.reactivando.futuro.entity.Usuario;
import com.reactivando.futuro.entity.Vacante;
import com.reactivando.futuro.exception.ForbiddenException;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.VacanteMapper;
import com.reactivando.futuro.repository.EmpresaRepository;
import com.reactivando.futuro.repository.UsuarioRepository;
import com.reactivando.futuro.repository.VacanteRepository;
import com.reactivando.futuro.repository.specification.VacanteSpecification;
import com.reactivando.futuro.service.VacanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VacanteServiceImpl implements VacanteService {

    private final VacanteRepository vacanteRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VacanteMapper vacanteMapper;
    private final com.reactivando.futuro.service.AuditoriaService auditoriaService;

    @Override
    @Transactional
    public VacanteResponseDTO crearVacante(String correoUsuario, VacanteRequestDTO requestDTO) {
        Empresa empresa = obtenerEmpresaPorCorreo(correoUsuario);

        Vacante vacante = vacanteMapper.toEntity(requestDTO);
        vacante.setEmpresa(empresa);

        if (vacante.getEstado() == null) {
            vacante.setEstado(EstadoVacante.ACTIVA);
        }

        Vacante guardada = vacanteRepository.save(vacante);
        auditoriaService.registrarEvento(
                correoUsuario,
                "CREACION_VACANTE",
                "Creación de vacante '" + guardada.getTitulo() + "' (ID: " + guardada.getId() + ") en la ciudad de " + guardada.getCiudad(),
                "127.0.0.1"
        );
        return vacanteMapper.toResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public VacanteResponseDTO obtenerPorId(Long id) {
        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + id));
        return vacanteMapper.toResponseDTO(vacante);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacanteResponseDTO> listarVacantes(Pageable pageable) {
        return vacanteRepository.findByEstado(EstadoVacante.ACTIVA, pageable)
                .map(vacanteMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacanteResponseDTO> buscarVacantes(
            String titulo,
            String ciudad,
            String tipoContrato,
            String nivelEstudio,
            BigDecimal salarioMin,
            BigDecimal salarioMax,
            EstadoVacante estado,
            Pageable pageable) {

        Specification<Vacante> spec = VacanteSpecification.filtrarVacantes(
                titulo, ciudad, tipoContrato, nivelEstudio, salarioMin, salarioMax, estado
        );

        return vacanteRepository.findAll(spec, pageable)
                .map(vacanteMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VacanteResponseDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        return vacanteRepository.findByEmpresaId(empresaId, pageable)
                .map(vacanteMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VacanteResponseDTO> listarTodasPorEmpresa(Long empresaId) {
        return vacanteRepository.findByEmpresaId(empresaId).stream()
                .map(vacanteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VacanteResponseDTO actualizarVacante(Long id, String correoUsuario, VacanteRequestDTO requestDTO) {
        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + id));

        validarPropiedadDeEmpresa(vacante, correoUsuario);

        vacante.setTitulo(requestDTO.getTitulo());
        vacante.setDescripcion(requestDTO.getDescripcion());
        vacante.setRequisitos(requestDTO.getRequisitos());
        vacante.setNivelEstudio(requestDTO.getNivelEstudio());
        vacante.setTipoContrato(requestDTO.getTipoContrato());
        vacante.setSalario(requestDTO.getSalario());
        vacante.setCiudad(requestDTO.getCiudad());
        vacante.setFechaCierre(requestDTO.getFechaCierre());
        if (requestDTO.getNumeroVacantes() != null) vacante.setNumeroVacantes(requestDTO.getNumeroVacantes());
        if (requestDTO.getEstado() != null) vacante.setEstado(requestDTO.getEstado());

        Vacante actualizada = vacanteRepository.save(vacante);
        auditoriaService.registrarEvento(
                correoUsuario,
                "EDICION_VACANTE",
                "Actualización de vacante '" + actualizada.getTitulo() + "' (ID: " + actualizada.getId() + ")",
                "127.0.0.1"
        );
        return vacanteMapper.toResponseDTO(actualizada);
    }

    @Override
    @Transactional
    public VacanteResponseDTO cambiarEstado(Long id, String correoUsuario, EstadoVacante nuevoEstado) {
        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + id));

        validarPropiedadDeEmpresa(vacante, correoUsuario);

        vacante.setEstado(nuevoEstado);
        Vacante actualizada = vacanteRepository.save(vacante);
        auditoriaService.registrarEvento(
                correoUsuario,
                "CAMBIO_ESTADO_VACANTE",
                "Cambio de estado de vacante '" + actualizada.getTitulo() + "' a " + nuevoEstado,
                "127.0.0.1"
        );
        return vacanteMapper.toResponseDTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminarVacante(Long id, String correoUsuario) {
        Vacante vacante = vacanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + id));

        validarPropiedadDeEmpresa(vacante, correoUsuario);

        vacanteRepository.delete(vacante);
        auditoriaService.registrarEvento(
                correoUsuario,
                "ELIMINACION_VACANTE",
                "Eliminación de vacante '" + vacante.getTitulo() + "' (ID: " + id + ")",
                "127.0.0.1"
        );
    }

    private Empresa obtenerEmpresaPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));

        if (usuario.getRol() != Rol.EMPRESA && usuario.getRol() != Rol.ADMIN) {
            throw new ForbiddenException("Solo los usuarios con rol EMPRESA o ADMIN pueden administrar vacantes");
        }

        return empresaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado para el usuario: " + correo));
    }

    private void validarPropiedadDeEmpresa(Vacante vacante, String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + correoUsuario));

        if (usuario.getRol() == Rol.ADMIN) {
            return; // Los administradores tienen acceso completo
        }

        Empresa empresa = empresaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado"));

        if (!vacante.getEmpresa().getId().equals(empresa.getId())) {
            throw new ForbiddenException("No tiene permisos para modificar esta vacante porque pertenece a otra empresa");
        }
    }
}
