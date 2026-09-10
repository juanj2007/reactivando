package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.recomendacion.RecomendacionVacanteDTO;

import java.util.List;

public interface RecomendacionService {
    List<RecomendacionVacanteDTO> obtenerRecomendacionesParaCandidato(String correoCandidato, int limite);
}
