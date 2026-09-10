package com.reactivando.futuro.controller;

import com.reactivando.futuro.dto.postulacion.CambiarEstadoPostulacionDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionRequestDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionResponseDTO;
import com.reactivando.futuro.service.PostulacionService;
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

@RestController
@RequestMapping("/api/postulaciones")
@RequiredArgsConstructor
public class PostulacionController {

    private final PostulacionService postulacionService;

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<PostulacionResponseDTO> postularse(
            Authentication authentication,
            @Valid @RequestBody PostulacionRequestDTO requestDTO) {
        PostulacionResponseDTO postulacion = postulacionService.postularse(authentication.getName(), requestDTO);
        return new ResponseEntity<>(postulacion, HttpStatus.CREATED);
    }

    @GetMapping("/mis-postulaciones")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<Page<PostulacionResponseDTO>> listarMisPostulaciones(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "fechaPostulacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postulacionService.listarMisPostulaciones(authentication.getName(), pageable));
    }

    @GetMapping("/empresa")
    @PreAuthorize("hasRole('EMPRESA') or hasRole('ADMIN')")
    public ResponseEntity<Page<PostulacionResponseDTO>> listarPostulacionesPorEmpresa(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "fechaPostulacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postulacionService.listarPostulacionesPorEmpresa(authentication.getName(), pageable));
    }

    @GetMapping("/vacante/{vacanteId}")
    @PreAuthorize("hasRole('EMPRESA') or hasRole('ADMIN')")
    public ResponseEntity<Page<PostulacionResponseDTO>> listarPostulacionesPorVacante(
            @PathVariable Long vacanteId,
            Authentication authentication,
            @PageableDefault(size = 10, sort = "fechaPostulacion", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postulacionService.listarPostulacionesPorVacante(vacanteId, authentication.getName(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostulacionResponseDTO> obtenerPorId(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(postulacionService.obtenerPorId(id, authentication.getName()));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('EMPRESA') or hasRole('ADMIN')")
    public ResponseEntity<PostulacionResponseDTO> cambiarEstado(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody CambiarEstadoPostulacionDTO dto) {
        return ResponseEntity.ok(postulacionService.cambiarEstadoPostulacion(id, authentication.getName(), dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CANDIDATO')")
    public ResponseEntity<Void> cancelarPostulacion(
            @PathVariable Long id,
            Authentication authentication) {
        postulacionService.cancelarPostulacion(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
