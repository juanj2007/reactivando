package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.UsuarioRequestDTO;
import com.reactivando.futuro.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO requestDTO);
    UsuarioResponseDTO obtenerPorId(Long id);
    UsuarioResponseDTO obtenerPorCorreo(String correo);
    List<UsuarioResponseDTO> obtenerTodos();
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO requestDTO);
    void cambiarEstadoUsuario(Long id, boolean estado);
    void eliminarUsuario(Long id);
}
