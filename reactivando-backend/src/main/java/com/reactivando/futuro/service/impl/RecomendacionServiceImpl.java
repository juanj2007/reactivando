package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.recomendacion.RecomendacionVacanteDTO;
import com.reactivando.futuro.entity.*;
import com.reactivando.futuro.exception.ForbiddenException;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.VacanteMapper;
import com.reactivando.futuro.repository.CandidatoRepository;
import com.reactivando.futuro.repository.UsuarioRepository;
import com.reactivando.futuro.repository.VacanteRepository;
import com.reactivando.futuro.service.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecomendacionServiceImpl implements RecomendacionService {

    private final CandidatoRepository candidatoRepository;
    private final VacanteRepository vacanteRepository;
    private final UsuarioRepository usuarioRepository;
    private final VacanteMapper vacanteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionVacanteDTO> obtenerRecomendacionesParaCandidato(String correoCandidato, int limite) {
        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);

        List<Vacante> vacantesActivas = vacanteRepository.findAll().stream()
                .filter(v -> v.getEstado() == EstadoVacante.ACTIVA)
                .collect(Collectors.toList());

        List<RecomendacionVacanteDTO> recomendaciones = new ArrayList<>();

        for (Vacante vacante : vacantesActivas) {
            RecomendacionVacanteDTO dto = calcularCoincidencia(candidato, vacante);
            if (dto.getPorcentajeCoincidencia() > 0) {
                recomendaciones.add(dto);
            }
        }

        // Ordenar descendentemente por porcentaje de coincidencia
        recomendaciones.sort((r1, r2) -> Integer.compare(r2.getPorcentajeCoincidencia(), r1.getPorcentajeCoincidencia()));

        return recomendaciones.stream()
                .limit(limite > 0 ? limite : 10)
                .collect(Collectors.toList());
    }

    private RecomendacionVacanteDTO calcularCoincidencia(Candidato candidato, Vacante vacante) {
        int puntajeTotal = 0;
        List<String> motivos = new ArrayList<>();

        // 1. Coincidencia en Ciudad (hasta 25 puntos)
        if (candidato.getCiudad() != null && vacante.getCiudad() != null) {
            if (candidato.getCiudad().trim().equalsIgnoreCase(vacante.getCiudad().trim())) {
                puntajeTotal += 25;
                motivos.add("Misma ciudad de residencia: " + vacante.getCiudad());
            }
        }

        // 2. Coincidencia en Nivel de Estudio (hasta 25 puntos)
        if (candidato.getNivelEstudio() != null && vacante.getNivelEstudio() != null) {
            String estudioCand = candidato.getNivelEstudio().trim().toLowerCase();
            String estudioVac = vacante.getNivelEstudio().trim().toLowerCase();

            if (estudioCand.equals(estudioVac) || estudioVac.contains(estudioCand) || estudioCand.contains(estudioVac)) {
                puntajeTotal += 25;
                motivos.add("Nivel de estudio acorde: " + vacante.getNivelEstudio());
            }
        }

        // 3. Coincidencia en Ocupación y Título (hasta 20 puntos)
        if (candidato.getOcupacion() != null && !candidato.getOcupacion().isBlank()) {
            String ocupacion = candidato.getOcupacion().trim().toLowerCase();
            String titulo = vacante.getTitulo().toLowerCase();
            String descripcion = vacante.getDescripcion().toLowerCase();

            if (titulo.contains(ocupacion) || ocupacion.contains(titulo)) {
                puntajeTotal += 20;
                motivos.add("Coincidencia directa con tu ocupación de " + candidato.getOcupacion());
            } else if (descripcion.contains(ocupacion)) {
                puntajeTotal += 10;
                motivos.add("La descripción menciona tu perfil de " + candidato.getOcupacion());
            }
        }

        // 4. Coincidencia en Habilidades vs Requisitos (hasta 30 puntos)
        if (candidato.getHabilidades() != null && !candidato.getHabilidades().isBlank()) {
            String[] habilidades = candidato.getHabilidades().split("[,;\\s]+");
            List<String> habilidadesCoincidentes = new ArrayList<>();
            String textoVacante = (vacante.getRequisitos() + " " + vacante.getDescripcion()).toLowerCase();

            for (String hab : habilidades) {
                String habLimpia = hab.trim().toLowerCase();
                if (habLimpia.length() > 2 && textoVacante.contains(habLimpia)) {
                    habilidadesCoincidentes.add(hab.trim());
                }
            }

            if (!habilidadesCoincidentes.isEmpty()) {
                int pesoPorHabilidad = Math.min(30, habilidadesCoincidentes.size() * 10);
                puntajeTotal += pesoPorHabilidad;
                motivos.add("Coincidencia en habilidades claves: " + String.join(", ", habilidadesCoincidentes));
            }
        }

        int porcentajeFinal = Math.min(100, puntajeTotal);

        return RecomendacionVacanteDTO.builder()
                .vacante(vacanteMapper.toResponseDTO(vacante))
                .porcentajeCoincidencia(porcentajeFinal)
                .motivosCoincidencia(motivos)
                .build();
    }

    private Candidato obtenerCandidatoPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));

        if (usuario.getRol() != Rol.CANDIDATO && usuario.getRol() != Rol.ADMIN) {
            throw new ForbiddenException("Solo los usuarios con rol CANDIDATO pueden recibir recomendaciones de vacantes");
        }

        return candidatoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de candidato no encontrado para el usuario: " + correo));
    }
}
