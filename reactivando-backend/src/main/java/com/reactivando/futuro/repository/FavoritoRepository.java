package com.reactivando.futuro.repository;

import com.reactivando.futuro.entity.Favorito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByCandidatoId(Long candidatoId);
    Page<Favorito> findByCandidatoId(Long candidatoId, Pageable pageable);
    Optional<Favorito> findByCandidatoIdAndVacanteId(Long candidatoId, Long vacanteId);
    Boolean existsByCandidatoIdAndVacanteId(Long candidatoId, Long vacanteId);
    void deleteByCandidatoIdAndVacanteId(Long candidatoId, Long vacanteId);
    long countByCandidatoId(Long candidatoId);
}
