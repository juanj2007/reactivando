package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.CandidatoResponseDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionRequestDTO;
import com.reactivando.futuro.dto.postulacion.PostulacionResponseDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.*;
import com.reactivando.futuro.exception.DuplicateResourceException;
import com.reactivando.futuro.external.EmailNotificationService;
import com.reactivando.futuro.mapper.PostulacionMapper;
import com.reactivando.futuro.repository.*;
import com.reactivando.futuro.service.impl.PostulacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostulacionServiceTest {

    @Mock
    private PostulacionRepository postulacionRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private VacanteRepository vacanteRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PostulacionMapper postulacionMapper;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private PostulacionServiceImpl postulacionService;

    private Usuario usuarioCandidato;
    private Candidato candidato;
    private Vacante vacante;
    private Postulacion postulacion;
    private PostulacionRequestDTO requestDTO;
    private PostulacionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        usuarioCandidato = Usuario.builder()
                .id(1L)
                .correo("candidato@test.com")
                .rol(Rol.CANDIDATO)
                .build();

        candidato = Candidato.builder()
                .id(20L)
                .usuario(usuarioCandidato)
                .build();

        vacante = Vacante.builder()
                .id(50L)
                .titulo("Backend Developer")
                .estado(EstadoVacante.ACTIVA)
                .build();

        postulacion = Postulacion.builder()
                .id(100L)
                .candidato(candidato)
                .vacante(vacante)
                .estado(EstadoPostulacion.PENDIENTE)
                .build();

        requestDTO = PostulacionRequestDTO.builder()
                .vacanteId(50L)
                .observaciones("Interesado en la propuesta")
                .build();

        responseDTO = PostulacionResponseDTO.builder()
                .id(100L)
                .candidato(CandidatoResponseDTO.builder().id(20L).build())
                .vacante(VacanteResponseDTO.builder().id(50L).titulo("Backend Developer").build())
                .estado(EstadoPostulacion.PENDIENTE)
                .build();
    }

    @Test
    @DisplayName("Debe permitir postularse exitosamente a una vacante activa")
    void postularseExitoso() {
        when(usuarioRepository.findByCorreo("candidato@test.com")).thenReturn(Optional.of(usuarioCandidato));
        when(candidatoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(candidato));
        when(vacanteRepository.findById(50L)).thenReturn(Optional.of(vacante));
        when(postulacionRepository.existsByCandidatoIdAndVacanteId(20L, 50L)).thenReturn(false);
        when(postulacionRepository.save(any(Postulacion.class))).thenReturn(postulacion);
        when(postulacionMapper.toResponseDTO(any(Postulacion.class))).thenReturn(responseDTO);

        PostulacionResponseDTO result = postulacionService.postularse("candidato@test.com", requestDTO);

        assertNotNull(result);
        assertEquals(50L, result.getVacante().getId());
        assertEquals("Backend Developer", result.getVacante().getTitulo());
        verify(postulacionRepository).save(any(Postulacion.class));
        verify(auditoriaService).registrarEvento(eq("candidato@test.com"), eq("POSTULACION"), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe lanzar DuplicateResourceException si el candidato ya está postulado")
    void postularseDuplicado() {
        when(usuarioRepository.findByCorreo("candidato@test.com")).thenReturn(Optional.of(usuarioCandidato));
        when(candidatoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(candidato));
        when(vacanteRepository.findById(50L)).thenReturn(Optional.of(vacante));
        when(postulacionRepository.existsByCandidatoIdAndVacanteId(20L, 50L)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> postulacionService.postularse("candidato@test.com", requestDTO));
        verify(postulacionRepository, never()).save(any());
    }
}
