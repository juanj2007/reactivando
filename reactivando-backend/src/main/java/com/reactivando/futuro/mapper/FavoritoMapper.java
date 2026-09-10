package com.reactivando.futuro.mapper;

import com.reactivando.futuro.dto.favorito.FavoritoResponseDTO;
import com.reactivando.futuro.entity.Favorito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoritoMapper {

    private final VacanteMapper vacanteMapper;

    public FavoritoResponseDTO toResponseDTO(Favorito entity) {
        if (entity == null) {
            return null;
        }
        return FavoritoResponseDTO.builder()
                .id(entity.getId())
                .candidatoId(entity.getCandidato().getId())
                .vacante(vacanteMapper.toResponseDTO(entity.getVacante()))
                .fechaAgregado(entity.getFechaAgregado())
                .build();
    }
}
