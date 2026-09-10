package com.reactivando.futuro.dto.postulacion;

import com.reactivando.futuro.dto.CandidatoResponseDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.EstadoPostulacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostulacionResponseDTO {
    private Long id;
    private CandidatoResponseDTO candidato;
    private VacanteResponseDTO vacante;
    private LocalDateTime fechaPostulacion;
    private EstadoPostulacion estado;
    private String observaciones;
}
