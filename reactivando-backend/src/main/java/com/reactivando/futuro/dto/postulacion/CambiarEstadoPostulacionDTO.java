package com.reactivando.futuro.dto.postulacion;

import com.reactivando.futuro.entity.EstadoPostulacion;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambiarEstadoPostulacionDTO {

    @NotNull(message = "El nuevo estado de la postulación es obligatorio")
    private EstadoPostulacion estado;

    private String observaciones;
}
