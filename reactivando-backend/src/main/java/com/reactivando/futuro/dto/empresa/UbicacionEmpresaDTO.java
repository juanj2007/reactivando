package com.reactivando.futuro.dto.empresa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UbicacionEmpresaDTO {
    private Long empresaId;
    private String nombreEmpresa;
    private String direccion;
    private String ciudad;
    private Double latitud;
    private Double longitud;
    private String googleMapsEmbedUrl;
}
