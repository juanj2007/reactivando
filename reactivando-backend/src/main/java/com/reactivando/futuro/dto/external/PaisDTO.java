package com.reactivando.futuro.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaisDTO {
    private String nombre;
    private String nombreOficial;
    private String codigo;
    private String capital;
    private String region;
    private Long poblacion;
    private String bandera;
    private String moneda;
}
