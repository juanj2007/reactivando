package com.reactivando.futuro.dto.vacante;

import com.reactivando.futuro.dto.EmpresaResponseDTO;
import com.reactivando.futuro.entity.EstadoVacante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacanteResponseDTO {
    private Long id;
    private EmpresaResponseDTO empresa;
    private String titulo;
    private String descripcion;
    private String requisitos;
    private String nivelEstudio;
    private String tipoContrato;
    private BigDecimal salario;
    private String ciudad;
    private LocalDateTime fechaPublicacion;
    private LocalDate fechaCierre;
    private EstadoVacante estado;
    private Integer numeroVacantes;
}
