package com.reactivando.futuro.service;

import com.reactivando.futuro.dto.EmpresaResponseDTO;
import com.reactivando.futuro.dto.vacante.VacanteRequestDTO;
import com.reactivando.futuro.dto.vacante.VacanteResponseDTO;
import com.reactivando.futuro.entity.*;
import com.reactivando.futuro.exception.ResourceNotFoundException;
import com.reactivando.futuro.mapper.VacanteMapper;
import com.reactivando.futuro.repository.EmpresaRepository;
import com.reactivando.futuro.repository.UsuarioRepository;
import com.reactivando.futuro.repository.VacanteRepository;
import com.reactivando.futuro.service.impl.VacanteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacanteServiceTest {

    @Mock
    private VacanteRepository vacanteRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VacanteMapper vacanteMapper;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private VacanteServiceImpl vacanteService;

    private Usuario usuarioEmpresa;
    private Empresa empresa;
    private Vacante vacante;
    private VacanteRequestDTO vacanteRequestDTO;
    private VacanteResponseDTO vacanteResponseDTO;

    @BeforeEach
    void setUp() {
        usuarioEmpresa = Usuario.builder()
                .id(10L)
                .correo("empresa@tech.com")
                .rol(Rol.EMPRESA)
                .build();

        empresa = Empresa.builder()
                .id(5L)
                .nombreEmpresa("Tech Solutions")
                .usuario(usuarioEmpresa)
                .build();

        vacante = Vacante.builder()
                .id(100L)
                .titulo("Desarrollador Java Senior")
                .descripcion("Buscamos experto Java Spring Boot")
                .requisitos("Java 17, Spring Boot, MySQL")
                .ciudad("Medellín")
                .salario(new BigDecimal("5000000"))
                .estado(EstadoVacante.ACTIVA)
                .empresa(empresa)
                .build();

        vacanteRequestDTO = VacanteRequestDTO.builder()
                .titulo("Desarrollador Java Senior")
                .descripcion("Buscamos experto Java Spring Boot")
                .requisitos("Java 17, Spring Boot, MySQL")
                .ciudad("Medellín")
                .salario(new BigDecimal("5000000"))
                .build();

        vacanteResponseDTO = VacanteResponseDTO.builder()
                .id(100L)
                .titulo("Desarrollador Java Senior")
                .ciudad("Medellín")
                .salario(new BigDecimal("5000000"))
                .estado(EstadoVacante.ACTIVA)
                .empresa(EmpresaResponseDTO.builder().id(5L).nombreEmpresa("Tech Solutions").build())
                .build();
    }

    @Test
    @DisplayName("Debe crear una vacante correctamente para una empresa autenticada")
    void crearVacanteExitoso() {
        when(usuarioRepository.findByCorreo("empresa@tech.com")).thenReturn(Optional.of(usuarioEmpresa));
        when(empresaRepository.findByUsuarioId(10L)).thenReturn(Optional.of(empresa));
        when(vacanteMapper.toEntity(any(VacanteRequestDTO.class))).thenReturn(vacante);
        when(vacanteRepository.save(any(Vacante.class))).thenReturn(vacante);
        when(vacanteMapper.toResponseDTO(any(Vacante.class))).thenReturn(vacanteResponseDTO);

        VacanteResponseDTO result = vacanteService.crearVacante("empresa@tech.com", vacanteRequestDTO);

        assertNotNull(result);
        assertEquals("Desarrollador Java Senior", result.getTitulo());
        assertEquals("Tech Solutions", result.getEmpresa().getNombreEmpresa());
        verify(vacanteRepository).save(any(Vacante.class));
        verify(auditoriaService).registrarEvento(eq("empresa@tech.com"), eq("CREACION_VACANTE"), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe obtener vacante por ID")
    void obtenerPorIdExitoso() {
        when(vacanteRepository.findById(100L)).thenReturn(Optional.of(vacante));
        when(vacanteMapper.toResponseDTO(vacante)).thenReturn(vacanteResponseDTO);

        VacanteResponseDTO result = vacanteService.obtenerPorId(100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si la vacante no existe")
    void obtenerPorIdNoEncontrada() {
        when(vacanteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vacanteService.obtenerPorId(999L));
    }

    @Test
    @DisplayName("Debe cambiar el estado de la vacante")
    void cambiarEstadoExitoso() {
        when(vacanteRepository.findById(100L)).thenReturn(Optional.of(vacante));
        when(usuarioRepository.findByCorreo("empresa@tech.com")).thenReturn(Optional.of(usuarioEmpresa));
        when(empresaRepository.findByUsuarioId(10L)).thenReturn(Optional.of(empresa));
        when(vacanteRepository.save(any(Vacante.class))).thenReturn(vacante);
        when(vacanteMapper.toResponseDTO(any(Vacante.class))).thenReturn(vacanteResponseDTO);

        VacanteResponseDTO result = vacanteService.cambiarEstado(100L, "empresa@tech.com", EstadoVacante.PAUSADA);

        assertNotNull(result);
        verify(vacanteRepository).save(vacante);
    }
}
