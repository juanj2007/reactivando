package com.reactivando.futuro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatoResponseDTO {
    private Long id;
    private UsuarioResponseDTO usuario;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String nivelEstudio;
    private String ocupacion;
    private String direccion;
    private String ciudad;
    private String descripcion;
    private String experiencia;
    private String habilidades;
    private String hojaDeVida;
    private String foto;
}
