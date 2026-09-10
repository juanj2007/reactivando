package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.postulacion.CambiarEstadoPostulacionDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionRequestDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionResponseDTO;
import com.reactivando.futuro.entity.*;
import com.reactivando.futuro.exception.BadRequestException;
import com.reactivando.futuro.exception.DuplicateResourceException;
import com.reactivando.futuro.exception.ForbiddenException;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.PostulacionMapper;
import com.reactivando.futuro.repository.*;
import com.reactivando.futuro.service.PostulacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostulacionServiceImpl implements PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final CandidatoRepository candidatoRepository;
    private final VacanteRepository vacanteRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostulacionMapper postulacionMapper;
    private final com.reactivando.futuro.external.EmailNotificationService emailNotificationService;
    private final com.reactivando.futuro.service.AuditoriaService auditoriaService;

    @Override
    @Transactional
    public PostulacionResponseDTO postularse(String correoCandidato, PostulacionRequestDTO requestDTO) {
        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);

        Vacante vacante = vacanteRepository.findById(requestDTO.getVacanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + requestDTO.getVacanteId()));

        if (vacante.getEstado() != EstadoVacante.ACTIVA) {
            throw new BadRequestException("No se puede postular a esta vacante porque su estado es: " + vacante.getEstado());
        }

        // Restricción anti-duplicados: (mismo candidato + misma vacante)
        if (postulacionRepository.existsByCandidatoIdAndVacanteId(candidato.getId(), vacante.getId())) {
            throw new DuplicateResourceException("Ya te has postulado previamente a la vacante: '" + vacante.getTitulo() + "'");
        }

        Postulacion postulacion = Postulacion.builder()
                .candidato(candidato)
                .vacante(vacante)
                .estado(EstadoPostulacion.PENDIENTE)
                .observaciones(requestDTO.getObservaciones())
                .build();

        Postulacion guardada = postulacionRepository.save(postulacion);
        
        // Enviar correo transaccional de postulación
        String nombreEmpresa = vacante.getEmpresa() != null ? vacante.getEmpresa().getNombreEmpresa() : "Empresa Confidencial";
        emailNotificationService.enviarNotificacionPostulacion(correoCandidato, vacante.getTitulo(), nombreEmpresa);

        auditoriaService.registrarEvento(
                correoCandidato,
                "POSTULACION",
                "El candidato realizó una postulación a la vacante '" + vacante.getTitulo() + "' (ID Vacante: " + vacante.getId() + ")",
                "127.0.0.1"
        );
        return postulacionMapper.toResponseDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public PostulacionResponseDTO obtenerPorId(Long id, String correoUsuario) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada con ID: " + id));

        validarAccesoAPostulacion(postulacion, correoUsuario);

        return postulacionMapper.toResponseDTO(postulacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostulacionResponseDTO> listarMisPostulaciones(String correoCandidato, Pageable pageable) {
        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);
        return postulacionRepository.findByCandidatoId(candidato.getId(), pageable)
                .map(postulacionMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostulacionResponseDTO> listarPostulacionesPorVacante(Long vacanteId, String correoEmpresa, Pageable pageable) {
        Vacante vacante = vacanteRepository.findById(vacanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + vacanteId));

        validarPropiedadDeEmpresaEnVacante(vacante, correoEmpresa);

        return postulacionRepository.findByVacanteId(vacanteId, pageable)
                .map(postulacionMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostulacionResponseDTO> listarPostulacionesPorEmpresa(String correoEmpresa, Pageable pageable) {
        Empresa empresa = obtenerEmpresaPorCorreo(correoEmpresa);
        return postulacionRepository.findByVacanteEmpresaId(empresa.getId(), pageable)
                .map(postulacionMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public PostulacionResponseDTO cambiarEstadoPostulacion(Long id, String correoEmpresa, CambiarEstadoPostulacionDTO dto) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada con ID: " + id));

        validarPropiedadDeEmpresaEnVacante(postulacion.getVacante(), correoEmpresa);

        postulacion.setEstado(dto.getEstado());
        if (dto.getObservaciones() != null) {
            postulacion.setObservaciones(dto.getObservaciones());
        }

        Postulacion actualizada = postulacionRepository.save(postulacion);
        
        // Enviar correo transaccional al candidato sobre el cambio de estado
        if (postulacion.getCandidato() != null && postulacion.getCandidato().getUsuario() != null) {
            emailNotificationService.enviarNotificacionCambioEstado(
                    postulacion.getCandidato().getUsuario().getCorreo(),
                    postulacion.getVacante().getTitulo(),
                    dto.getEstado().name()
            );
        }

        auditoriaService.registrarEvento(
                correoEmpresa,
                "CAMBIO_ESTADO_POSTULACION",
                "La empresa cambió el estado de la postulación ID " + id + " a " + dto.getEstado(),
                "127.0.0.1"
        );
        return postulacionMapper.toResponseDTO(actualizada);
    }

    @Override
    @Transactional
    public void cancelarPostulacion(Long id, String correoCandidato) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada con ID: " + id));

        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);
        if (!postulacion.getCandidato().getId().equals(candidato.getId())) {
            throw new ForbiddenException("No tiene permisos para cancelar una postulación que no le pertenece");
        }

        postulacionRepository.delete(postulacion);
    }

    private Candidato obtenerCandidatoPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));

        if (usuario.getRol() != Rol.CANDIDATO && usuario.getRol() != Rol.ADMIN) {
            throw new ForbiddenException("Solo los usuarios con rol CANDIDATO pueden realizar postulaciones");
        }

        return candidatoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de candidato no encontrado para el usuario: " + correo));
    }

    private Empresa obtenerEmpresaPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));

        if (usuario.getRol() != Rol.EMPRESA && usuario.getRol() != Rol.ADMIN) {
            throw new ForbiddenException("Solo los usuarios con rol EMPRESA o ADMIN pueden gestionar postulaciones recibidas");
        }

        return empresaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado para el usuario: " + correo));
    }

    private void validarPropiedadDeEmpresaEnVacante(Vacante vacante, String correoEmpresa) {
        Usuario usuario = usuarioRepository.findByCorreo(correoEmpresa)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (usuario.getRol() == Rol.ADMIN) return;

        Empresa empresa = empresaRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empresa no encontrado"));

        if (!vacante.getEmpresa().getId().equals(empresa.getId())) {
            throw new ForbiddenException("No tiene permisos para acceder a las postulaciones de una vacante pertenecientes a otra empresa");
        }
    }

    private void validarAccesoAPostulacion(Postulacion postulacion, String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (usuario.getRol() == Rol.ADMIN) return;

        if (usuario.getRol() == Rol.CANDIDATO) {
            Candidato candidato = candidatoRepository.findByUsuarioId(usuario.getId()).orElse(null);
            if (candidato != null && postulacion.getCandidato().getId().equals(candidato.getId())) return;
        }

        if (usuario.getRol() == Rol.EMPRESA) {
            Empresa empresa = empresaRepository.findByUsuarioId(usuario.getId()).orElse(null);
            if (empresa != null && postulacion.getVacante().getEmpresa().getId().equals(empresa.getId())) return;
        }

        throw new ForbiddenException("No tiene permisos para ver esta postulación");
    }
}
