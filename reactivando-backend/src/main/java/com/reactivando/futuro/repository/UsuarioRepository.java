package com.reactivando.futuro.repository;

import com.reactivando.futuro.entity.Rol;
import com.reactivando.futuro.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Boolean existsByCorreo(String correo);
    List<Usuario> findByRol(Rol rol);
}
