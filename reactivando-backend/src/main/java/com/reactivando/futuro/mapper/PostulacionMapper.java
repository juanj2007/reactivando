package com.reactivando.futuro.mapper;

import com.reactivando.futuro.dto.postulacion.PostulacionResponseDTO;
import com.reactivando.futuro.entity.Postulacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostulacionMapper {

    private final CandidatoMapper candidatoMapper;
    private final VacanteMapper vacanteMapper;

    public PostulacionResponseDTO toResponseDTO(Postulacion entity) {
        if (entity == null) {
            return null;
        }
        return PostulacionResponseDTO.builder()
                .id(entity.getId())
                .candidato(candidatoMapper.toResponseDTO(entity.getCandidato()))
                .vacante(vacanteMapper.toResponseDTO(entity.getVacante()))
                .fechaPostulacion(entity.getFechaPostulacion())
                .estado(entity.getEstado())
                .observaciones(entity.getObservaciones())
                .build();
    }
}
