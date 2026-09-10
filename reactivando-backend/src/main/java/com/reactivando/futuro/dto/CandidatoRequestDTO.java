package com.reactivando.futuro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatoRequestDTO {

    private LocalDate fechaNacimiento;

    @Min(value = 14, message = "La edad mínima es 14 años")
    private Integer edad;

    @Size(max = 100, message = "El nivel de estudio no debe exceder 100 caracteres")
    private String nivelEstudio;

    @Size(max = 150, message = "La ocupación no debe exceder 150 caracteres")
    private String ocupacion;

    @Size(max = 200, message = "La dirección no debe exceder 200 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
    private String ciudad;

    private String descripcion;
    private String experiencia;
    private String habilidades;
    private String hojaDeVida;
    private String foto;
}
