package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.CandidatoRequestDTO;
import com.reactivando.futuro.dto.CandidatoResponseDTO;
import com.reactivando.futuro.service.CandidatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
@RequiredArgsConstructor
public class CandidatoController {

    private final CandidatoService candidatoService;

    @GetMapping("/{id}")
    public ResponseEntity<CandidatoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(candidatoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CandidatoResponseDTO> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(candidatoService.obtenerPorUsuarioId(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<CandidatoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(candidatoService.obtenerTodos());
    }

    @PutMapping("/usuario/{usuarioId}")
    public ResponseEntity<CandidatoResponseDTO> actualizarPerfil(
            @PathVariable Long usuarioId,
            @Valid @RequestBody CandidatoRequestDTO requestDTO) {
        return ResponseEntity.ok(candidatoService.actualizarPerfil(usuarioId, requestDTO));
    }
}
