package com.reactivando.futuro.dto.vacante;

import com.reactivando.futuro.entity.EstadoVacante;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacanteRequestDTO {

    @NotBlank(message = "El título de la vacante es obligatorio")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción de la vacante es obligatoria")
    private String descripcion;

    @NotBlank(message = "Los requisitos de la vacante son obligatorios")
    private String requisitos;

    @Size(max = 100, message = "El nivel de estudio no debe exceder 100 caracteres")
    private String nivelEstudio;

    @Size(max = 100, message = "El tipo de contrato no debe exceder 100 caracteres")
    private String tipoContrato;

    private BigDecimal salario;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
    private String ciudad;

    private LocalDate fechaCierre;

    private EstadoVacante estado;

    @NotNull(message = "El número de vacantes es obligatorio")
    @Min(value = 1, message = "Debe ofrecer al menos 1 vacante")
    private Integer numeroVacantes;
}
