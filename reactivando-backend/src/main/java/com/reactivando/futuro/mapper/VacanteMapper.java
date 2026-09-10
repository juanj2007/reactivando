package com.reactivando.futuro.mapper;

import com.reactivando.futuro.dto.vacante.VacanteRequestDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.Vacante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VacanteMapper {

    private final EmpresaMapper empresaMapper;

    public Vacante toEntity(VacanteRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Vacante.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .requisitos(dto.getRequisitos())
                .nivelEstudio(dto.getNivelEstudio())
                .tipoContrato(dto.getTipoContrato())
                .salario(dto.getSalario())
                .ciudad(dto.getCiudad())
                .fechaCierre(dto.getFechaCierre())
                .estado(dto.getEstado())
                .numeroVacantes(dto.getNumeroVacantes())
                .build();
    }

    public VacanteResponseDTO toResponseDTO(Vacante entity) {
        if (entity == null) {
            return null;
        }
        return VacanteResponseDTO.builder()
                .id(entity.getId())
                .empresa(empresaMapper.toResponseDTO(entity.getEmpresa()))
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .requisitos(entity.getRequisitos())
                .nivelEstudio(entity.getNivelEstudio())
                .tipoContrato(entity.getTipoContrato())
                .salario(entity.getSalario())
                .ciudad(entity.getCiudad())
                .fechaPublicacion(entity.getFechaPublicacion())
                .fechaCierre(entity.getFechaCierre())
                .estado(entity.getEstado())
                .numeroVacantes(entity.getNumeroVacantes())
                .build();
    }
}
