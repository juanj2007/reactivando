package com.reactivando.futuro.repository;

import com.reactivando.futuro.entity.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuarioId(Long usuarioId);
    Page<Auditoria> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Auditoria> findAllByOrderByFechaDesc(Pageable pageable);
}
