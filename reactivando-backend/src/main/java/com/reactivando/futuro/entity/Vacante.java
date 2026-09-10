package com.reactivando.futuro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requisitos;

    @Column(name = "nivel_estudio", length = 100)
    private String nivelEstudio;

    @Column(name = "tipo_contrato", length = 100)
    private String tipoContrato;

    @Column(precision = 12, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_cierre")
    private LocalDate fechaCierre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoVacante estado = EstadoVacante.ACTIVA;

    @Column(name = "numero_vacantes")
    @Builder.Default
    private Integer numeroVacantes = 1;

    @PrePersist
    protected void onCreate() {
        this.fechaPublicacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoVacante.ACTIVA;
        }
        if (this.numeroVacantes == null) {
            this.numeroVacantes = 1;
        }
    }
}
