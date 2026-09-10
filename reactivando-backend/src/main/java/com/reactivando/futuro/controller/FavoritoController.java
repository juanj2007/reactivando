package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.favorito.FavoritoResponseDTO;
import com.reactivando.futuro.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @PostMapping("/vacante/{vacanteId}")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<FavoritoResponseDTO> agregarFavorito(
            @PathVariable Long vacanteId,
            Authentication authentication) {
        FavoritoResponseDTO favorito = favoritoService.agregarFavorito(authentication.getName(), vacanteId);
        return new ResponseEntity<>(favorito, HttpStatus.CREATED);
    }

    @DeleteMapping("/vacante/{vacanteId}")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<Void> eliminarFavorito(
            @PathVariable Long vacanteId,
            Authentication authentication) {
        favoritoService.eliminarFavorito(authentication.getName(), vacanteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<Page<FavoritoResponseDTO>> listarMisFavoritos(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "fechaAgregado", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(favoritoService.listarMisFavoritos(authentication.getName(), pageable));
    }

    @GetMapping("/vacante/{vacanteId}/check")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<Boolean> esFavorito(
            @PathVariable Long vacanteId,
            Authentication authentication) {
        return ResponseEntity.ok(favoritoService.esFavorito(authentication.getName(), vacanteId));
    }
}
