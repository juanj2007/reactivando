package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.favorito.FavoritoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoritoService {
    FavoritoResponseDTO agregarFavorito(String correoCandidato, Long vacanteId);
    void eliminarFavorito(String correoCandidato, Long vacanteId);
    Page<FavoritoResponseDTO> listarMisFavoritos(String correoCandidato, Pageable pageable);
    boolean esFavorito(String correoCandidato, Long vacanteId);
}
