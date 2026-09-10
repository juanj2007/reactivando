package com.reactivando.futuro.dto.favorito;

import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoResponseDTO {
    private Long id;
    private Long candidatoId;
    private VacanteResponseDTO vacante;
    private LocalDateTime fechaAgregado;
}
