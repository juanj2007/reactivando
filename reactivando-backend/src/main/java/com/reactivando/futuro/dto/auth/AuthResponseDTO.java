package com.reactivando.futuro.dto.auth;

import com.reactivando.futuro.dto.UsuarioResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private UsuarioResponseDTO usuario;
}
