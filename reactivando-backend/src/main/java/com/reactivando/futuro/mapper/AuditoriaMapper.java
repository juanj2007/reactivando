package com.reactivando.futuro.mapper;

import com.reactivando.futuro.dto.auditoria.AuditoriaResponseDTO;
import com.reactivando.futuro.entity.Auditoria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditoriaMapper {

    private final UsuarioMapper usuarioMapper;

    public AuditoriaResponseDTO toResponseDTO(Auditoria entity) {
        if (entity == null) {
            return null;
        }
        return AuditoriaResponseDTO.builder()
                .id(entity.getId())
                .usuario(usuarioMapper.toResponseDTO(entity.getUsuario()))
                .accion(entity.getAccion())
                .descripcion(entity.getDescripcion())
                .fecha(entity.getFecha())
                .ip(entity.getIp())
                .build();
    }
}
