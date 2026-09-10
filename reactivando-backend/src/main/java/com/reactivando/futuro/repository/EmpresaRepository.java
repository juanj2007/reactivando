package com.reactivando.futuro.repository;

import com.reactivando.futuro.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByUsuarioId(Long usuarioId);
    Optional<Empresa> findByUsuarioCorreo(String correo);
    Boolean existsByNit(String nit);
}
