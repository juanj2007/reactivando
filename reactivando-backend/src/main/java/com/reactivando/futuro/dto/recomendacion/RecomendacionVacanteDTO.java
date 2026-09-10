package com.reactivando.futuro.dto.recomendacion;

import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecomendacionVacanteDTO {
    private VacanteResponseDTO vacante;
    private Integer porcentajeCoincidencia;
    private List<String> motivosCoincidencia;
}
