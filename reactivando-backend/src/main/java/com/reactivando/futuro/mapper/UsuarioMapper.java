package com.reactivando.futuro.mapper;

import com.reactivando.futuro.dto.UsuarioRequestDTO;
import com.reactivando.futuro.dto.UsuarioResponseDTO;
import com.reactivando.futuro.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .correo(dto.getCorreo())
                .password(dto.getPassword())
                .telefono(dto.getTelefono())
                .rol(dto.getRol())
                .build();
    }

    public UsuarioResponseDTO toResponseDTO(Usuario entity) {
        if (entity == null) {
            return null;
        }
        return UsuarioResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .correo(entity.getCorreo())
                .telefono(entity.getTelefono())
                .rol(entity.getRol())
                .estado(entity.getEstado())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }
}
