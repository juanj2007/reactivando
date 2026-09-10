package com.reactivando.futuro.repository;

import com.reactivando.futuro.entity.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    Optional<Candidato> findByUsuarioId(Long usuarioId);
    Optional<Candidato> findByUsuarioCorreo(String correo);
}
