package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.vacante.VacanteRequestDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.EstadoVacante;
import com.reactivando.futuro.service.VacanteService;
import jakarta.validation.Valid;
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

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/vacantes")
@RequiredArgsConstructor
public class VacanteController {

    private final VacanteService vacanteService;

    @GetMapping
    public ResponseEntity<Page<VacanteResponseDTO>> listarVacantes(
            @PageableDefault(size = 10, sort = "fechaPublicacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(vacanteService.listarVacantes(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<VacanteResponseDTO>> buscarVacantes(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String tipoContrato,
            @RequestParam(required = false) String nivelEstudio,
            @RequestParam(required = false) BigDecimal salarioMin,
            @RequestParam(required = false) BigDecimal salarioMax,
            @RequestParam(required = false) EstadoVacante estado,
            @PageableDefault(size = 10, sort = "fechaPublicacion", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(vacanteService.buscarVacantes(
                titulo, ciudad, tipoContrato, nivelEstudio, salarioMin, salarioMax, estado, pageable
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacanteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vacanteService.obtenerPorId(id));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<Page<VacanteResponseDTO>> listarPorEmpresa(
            @PathVariable Long empresaId,
            @PageableDefault(size = 10, sort = "fechaPublicacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(vacanteService.listarPorEmpresa(empresaId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('EMPRESA') or hasRole('ADMIN')")
    public ResponseEntity<VacanteResponseDTO> crearVacante(
            Authentication authentication,
            @Valid @RequestBody VacanteRequestDTO requestDTO) {
        VacanteResponseDTO nuevaVacante = vacanteService.crearVacante(authentication.getName(), requestDTO);
        return new ResponseEntity<>(nuevaVacante, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPRESA') or hasRole('ADMIN')")
    public ResponseEntity<VacanteResponseDTO> actualizarVacante(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody VacanteRequestDTO requestDTO) {
        return ResponseEntity.ok(vacanteService.actualizarVacante(id, authentication.getName(), requestDTO));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('EMPRESA') or hasRole('ADMIN')")
    public ResponseEntity<VacanteResponseDTO> cambiarEstado(
            @PathVariable Long id,
            Authentication authentication,
            @RequestParam EstadoVacante estado) {
        return ResponseEntity.ok(vacanteService.cambiarEstado(id, authentication.getName(), estado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPRESA') or hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarVacante(
            @PathVariable Long id,
            Authentication authentication) {
        vacanteService.eliminarVacante(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
