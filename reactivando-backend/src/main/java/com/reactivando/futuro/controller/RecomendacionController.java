package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.recomendacion.RecomendacionVacanteDTO;
import com.reactivando.futuro.service.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<List<RecomendacionVacanteDTO>> obtenerRecomendaciones(
            Authentication authentication,
            @RequestParam(defaultValue = "10") int limite) {
        List<RecomendacionVacanteDTO> recomendaciones = recomendacionService.obtenerRecomendacionesParaCandidato(
                authentication.getName(), limite
        );
        return ResponseEntity.ok(recomendaciones);
    }
}
