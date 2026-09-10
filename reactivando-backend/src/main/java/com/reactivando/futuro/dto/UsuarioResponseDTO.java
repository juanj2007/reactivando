package com.reactivando.futuro.dto;

import com.reactivando.futuro.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private Rol rol;
    private Boolean estado;
    private LocalDateTime fechaRegistro;
}
