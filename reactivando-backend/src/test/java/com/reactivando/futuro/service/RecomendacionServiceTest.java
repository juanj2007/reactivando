package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.recomendacion.RecomendacionVacanteDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.*;
import com.reactivando.futuro.mapper.VacanteMapper;
import com.reactivando.futuro.repository.CandidatoRepository;
import com.reactivando.futuro.repository.UsuarioRepository;
import com.reactivando.futuro.repository.VacanteRepository;
import com.reactivando.futuro.service.impl.RecomendacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecomendacionServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private VacanteRepository vacanteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VacanteMapper vacanteMapper;

    @InjectMocks
    private RecomendacionServiceImpl recomendacionService;

    private Usuario usuario;
    private Candidato candidato;
    private Vacante vacanteMatch;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .id(1L)
                .correo("candidato@test.com")
                .rol(Rol.CANDIDATO)
                .build();

        candidato = Candidato.builder()
                .id(10L)
                .usuario(usuario)
                .ciudad("Medellín")
                .nivelEstudio("Profesional")
                .ocupacion("Desarrollador")
                .habilidades("Java, Spring, MySQL")
                .build();

        vacanteMatch = Vacante.builder()
                .id(100L)
                .titulo("Desarrollador Java")
                .ciudad("Medellín")
                .nivelEstudio("Profesional")
                .requisitos("Conocimientos en Java y Spring Boot")
                .descripcion("Puesto de desarrollador con MySQL")
                .estado(EstadoVacante.ACTIVA)
                .build();
    }

    @Test
    @DisplayName("Debe calcular porcentaje de coincidencia alto para candidato con habilidades y ciudad coincidentes")
    void obtenerRecomendacionesCalculoMatch() {
        when(usuarioRepository.findByCorreo("candidato@test.com")).thenReturn(Optional.of(usuario));
        when(candidatoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(candidato));
        when(vacanteRepository.findAll()).thenReturn(List.of(vacanteMatch));
        when(vacanteMapper.toResponseDTO(any(Vacante.class))).thenReturn(
                VacanteResponseDTO.builder().id(100L).titulo("Desarrollador Java").build()
        );

        List<RecomendacionVacanteDTO> recomendaciones = recomendacionService.obtenerRecomendacionesParaCandidato("candidato@test.com", 5);

        assertFalse(recomendaciones.isEmpty());
        assertEquals(1, recomendaciones.size());
        assertTrue(recomendaciones.get(0).getPorcentajeCoincidencia() > 50);
        assertFalse(recomendaciones.get(0).getMotivosCoincidencia().isEmpty());
    }
}
