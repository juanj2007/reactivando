package com.reactivando.futuro.mapper;

import com.reactivando.futuro.dto.CandidatoRequestDTO;
import com.reactivando.futuro.dto.CandidatoResponseDTO;
import com.reactivando.futuro.entity.Candidato;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidatoMapper {

    private final UsuarioMapper usuarioMapper;

    public Candidato toEntity(CandidatoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Candidato.builder()
                .fechaNacimiento(dto.getFechaNacimiento())
                .edad(dto.getEdad())
                .nivelEstudio(dto.getNivelEstudio())
                .ocupacion(dto.getOcupacion())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .descripcion(dto.getDescripcion())
                .experiencia(dto.getExperiencia())
                .habilidades(dto.getHabilidades())
                .hojaDeVida(dto.getHojaDeVida())
                .foto(dto.getFoto())
                .build();
    }

    public CandidatoResponseDTO toResponseDTO(Candidato entity) {
        if (entity == null) {
            return null;
        }
        return CandidatoResponseDTO.builder()
                .id(entity.getId())
                .usuario(usuarioMapper.toResponseDTO(entity.getUsuario()))
                .fechaNacimiento(entity.getFechaNacimiento())
                .edad(entity.getEdad())
                .nivelEstudio(entity.getNivelEstudio())
                .ocupacion(entity.getOcupacion())
                .direccion(entity.getDireccion())
                .ciudad(entity.getCiudad())
                .descripcion(entity.getDescripcion())
                .experiencia(entity.getExperiencia())
                .habilidades(entity.getHabilidades())
                .hojaDeVida(entity.getHojaDeVida())
                .foto(entity.getFoto())
                .build();
    }
}
