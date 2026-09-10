package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.UsuarioRequestDTO;
import com.reactivando.futuro.dto.UsuarioResponseDTO;
import com.reactivando.futuro.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO nuevoUsuario = usuarioService.registrarUsuario(requestDTO);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(usuarioService.obtenerPorCorreo(correo));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, requestDTO));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean estado) {
        usuarioService.cambiarEstadoUsuario(id, estado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
