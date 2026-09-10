package com.reactivando.futuro.dto.auditoria;

import com.reactivando.futuro.dto.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaResponseDTO {
    private Long id;
    private UsuarioResponseDTO usuario;
    private String accion;
    private String descripcion;
    private LocalDateTime fecha;
    private String ip;
}
