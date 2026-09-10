package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.external.PaisDTO;
import com.reactivando.futuro.external.CountryApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paises")
@RequiredArgsConstructor
public class PaisController {

    private final CountryApiService countryApiService;

    @GetMapping
    public ResponseEntity<List<PaisDTO>> obtenerPaises(
            @RequestParam(required = false) String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return ResponseEntity.ok(countryApiService.buscarPorNombre(nombre));
        }
        return ResponseEntity.ok(countryApiService.obtenerTodosLosPaises());
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<PaisDTO>> obtenerPorRegion(@PathVariable String region) {
        return ResponseEntity.ok(countryApiService.buscarPorRegion(region));
    }
}
