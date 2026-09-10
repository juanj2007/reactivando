package com.reactivando.futuro.service.impl;

import com.reactivando.futuro.dto.favorito.FavoritoResponseDTO;
import com.reactivando.futuro.entity.Candidato;
import com.reactivando.futuro.entity.Favorito;
import com.reactivando.futuro.entity.Rol;
import com.reactivando.futuro.entity.Usuario;
import com.reactivando.futuro.entity.Vacante;
import com.reactivando.futuro.exception.DuplicateResourceException;
import com.reactivando.futuro.exception.ForbiddenException;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.FavoritoMapper;
import com.reactivando.futuro.repository.CandidatoRepository;
import com.reactivando.futuro.repository.FavoritoRepository;
import com.reactivando.futuro.repository.UsuarioRepository;
import com.reactivando.futuro.repository.VacanteRepository;
import com.reactivando.futuro.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final CandidatoRepository candidatoRepository;
    private final VacanteRepository vacanteRepository;
    private final UsuarioRepository usuarioRepository;
    private final FavoritoMapper favoritoMapper;

    @Override
    @Transactional
    public FavoritoResponseDTO agregarFavorito(String correoCandidato, Long vacanteId) {
        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);

        Vacante vacante = vacanteRepository.findById(vacanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacante no encontrada con ID: " + vacanteId));

        if (favoritoRepository.existsByCandidatoIdAndVacanteId(candidato.getId(), vacante.getId())) {
            throw new DuplicateResourceException("La vacante '" + vacante.getTitulo() + "' ya se encuentra en tus favoritos");
        }

        Favorito favorito = Favorito.builder()
                .candidato(candidato)
                .vacante(vacante)
                .build();

        Favorito guardado = favoritoRepository.save(favorito);
        return favoritoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public void eliminarFavorito(String correoCandidato, Long vacanteId) {
        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);

        if (!favoritoRepository.existsByCandidatoIdAndVacanteId(candidato.getId(), vacanteId)) {
            throw new ResourceNotFoundException("La vacante no se encuentra marcada como favorita");
        }

        favoritoRepository.deleteByCandidatoIdAndVacanteId(candidato.getId(), vacanteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FavoritoResponseDTO> listarMisFavoritos(String correoCandidato, Pageable pageable) {
        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);
        return favoritoRepository.findByCandidatoId(candidato.getId(), pageable)
                .map(favoritoMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean esFavorito(String correoCandidato, Long vacanteId) {
        Candidato candidato = obtenerCandidatoPorCorreo(correoCandidato);
        return favoritoRepository.existsByCandidatoIdAndVacanteId(candidato.getId(), vacanteId);
    }

    private Candidato obtenerCandidatoPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));

        if (usuario.getRol() != Rol.CANDIDATO && usuario.getRol() != Rol.ADMIN) {
            throw new ForbiddenException("Solo los usuarios con rol CANDIDATO pueden gestionar favoritos");
        }

        return candidatoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de candidato no encontrado para el usuario: " + correo));
    }
}
