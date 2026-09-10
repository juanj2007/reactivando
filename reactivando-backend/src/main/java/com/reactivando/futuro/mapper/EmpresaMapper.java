package com.reactivando.futuro.mapper;

import com.reactivando.futuro.dto.EmpresaRequestDTO;
import com.reactivando.futuro.dto.EmpresaResponseDTO;
import com.reactivando.futuro.entity.Empresa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmpresaMapper {

    private final UsuarioMapper usuarioMapper;

    public Empresa toEntity(EmpresaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Empresa.builder()
                .nombreEmpresa(dto.getNombreEmpresa())
                .nit(dto.getNit())
                .descripcion(dto.getDescripcion())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .telefono(dto.getTelefono())
                .correoCorporativo(dto.getCorreoCorporativo())
                .numeroEmpleados(dto.getNumeroEmpleados())
                .sitioWeb(dto.getSitioWeb())
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .build();
    }

    public EmpresaResponseDTO toResponseDTO(Empresa entity) {
        if (entity == null) {
            return null;
        }
        return EmpresaResponseDTO.builder()
                .id(entity.getId())
                .usuario(usuarioMapper.toResponseDTO(entity.getUsuario()))
                .nombreEmpresa(entity.getNombreEmpresa())
                .nit(entity.getNit())
                .descripcion(entity.getDescripcion())
                .direccion(entity.getDireccion())
                .ciudad(entity.getCiudad())
                .telefono(entity.getTelefono())
                .correoCorporativo(entity.getCorreoCorporativo())
                .numeroEmpleados(entity.getNumeroEmpleados())
                .sitioWeb(entity.getSitioWeb())
                .latitud(entity.getLatitud())
                .longitud(entity.getLongitud())
                .fechaRegistro(entity.getFechaRegistro())
                .estado(entity.getEstado())
                .build();
    }
}
