package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.UsuarioResponseDTO;
import com.reactivando.futuro.dto.auth.AuthResponseDTO;
import com.reactivando.futuro.dto.auth.LoginRequestDTO;
import com.reactivando.futuro.dto.auth.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO loginDTO);
    AuthResponseDTO register(RegisterRequestDTO registerDTO);
    UsuarioResponseDTO obtenerUsuarioActual(String correo);
}
