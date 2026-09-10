package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.EmpresaRequestDTO;
import com.reactivando.futuro.dto.EmpresaResponseDTO;
import com.reactivando.futuro.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.obtenerPorId(id));
    }

    @GetMapping("/{id}/ubicacion")
    public ResponseEntity<com.reactivando.futuro.dto.empresa.UbicacionEmpresaDTO> obtenerUbicacion(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.obtenerUbicacion(id));
    }

    @PutMapping("/usuario/{usuarioId}/coordenadas")
    public ResponseEntity<EmpresaResponseDTO> actualizarCoordenadas(
            @PathVariable Long usuarioId,
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        return ResponseEntity.ok(empresaService.actualizarCoordenadas(usuarioId, latitud, longitud));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EmpresaResponseDTO> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(empresaService.obtenerPorUsuarioId(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(empresaService.obtenerTodas());
    }

    @PutMapping("/usuario/{usuarioId}")
    public ResponseEntity<EmpresaResponseDTO> actualizarPerfil(
            @PathVariable Long usuarioId,
            @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        return ResponseEntity.ok(empresaService.actualizarPerfil(usuarioId, requestDTO));
    }
}
