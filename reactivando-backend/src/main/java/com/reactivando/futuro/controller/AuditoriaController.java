package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.auditoria.AuditoriaResponseDTO;
import com.reactivando.futuro.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditoriaResponseDTO>> listarEventos(
            @PageableDefault(size = 20, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(auditoriaService.listarEventos(pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditoriaResponseDTO>> listarEventosPorUsuario(
            @PathVariable Long usuarioId,
            @PageableDefault(size = 20, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(auditoriaService.listarEventosPorUsuario(usuarioId, pageable));
    }
}
