package com.reactivando.futuro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaResponseDTO {
    private Long id;
    private UsuarioResponseDTO usuario;
    private String nombreEmpresa;
    private String nit;
    private String descripcion;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String correoCorporativo;
    private Integer numeroEmpleados;
    private String sitioWeb;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaRegistro;
    private Boolean estado;
}
