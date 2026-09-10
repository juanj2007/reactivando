package com.reactivando.futuro.repository;

import com.reactivando.futuro.entity.EstadoPostulacion;
import com.reactivando.futuro.entity.Postulacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {
    List<Postulacion> findByCandidatoId(Long candidatoId);
    Page<Postulacion> findByCandidatoId(Long candidatoId, Pageable pageable);
    List<Postulacion> findByVacanteId(Long vacanteId);
    Page<Postulacion> findByVacanteId(Long vacanteId, Pageable pageable);
    List<Postulacion> findByVacanteEmpresaId(Long empresaId);
    Page<Postulacion> findByVacanteEmpresaId(Long empresaId, Pageable pageable);
    Optional<Postulacion> findByCandidatoIdAndVacanteId(Long candidatoId, Long vacanteId);
    Boolean existsByCandidatoIdAndVacanteId(Long candidatoId, Long vacanteId);
    long countByCandidatoIdAndEstado(Long candidatoId, EstadoPostulacion estado);
    long countByVacanteEmpresaIdAndEstado(Long empresaId, EstadoPostulacion estado);
}
