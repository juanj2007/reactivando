package com.reactivando.futuro.repository;

import com.reactivando.futuro.entity.EstadoVacante;
import com.reactivando.futuro.entity.Vacante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacanteRepository extends JpaRepository<Vacante, Long>, JpaSpecificationExecutor<Vacante> {
    Page<Vacante> findByEstado(EstadoVacante estado, Pageable pageable);
    List<Vacante> findByEmpresaId(Long empresaId);
    Page<Vacante> findByEmpresaId(Long empresaId, Pageable pageable);
    long countByEmpresaIdAndEstado(Long empresaId, EstadoVacante estado);
}
