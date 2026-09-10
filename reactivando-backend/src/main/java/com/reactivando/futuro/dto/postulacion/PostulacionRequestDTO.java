package com.reactivando.futuro.dto.postulacion;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulacionRequestDTO {

    @NotNull(message = "El ID de la vacante es obligatorio")
    private Long vacanteId;

    private String observaciones;
}
