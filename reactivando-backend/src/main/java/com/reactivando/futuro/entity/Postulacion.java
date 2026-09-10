package com.reactivando.futuro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "postulaciones",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_candidato_vacante", columnNames = {"candidato_id", "vacante_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacante_id", nullable = false)
    private Vacante vacante;

    @Column(name = "fecha_postulacion", nullable = false, updatable = false)
    private LocalDateTime fechaPostulacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoPostulacion estado = EstadoPostulacion.PENDIENTE;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @PrePersist
    protected void onCreate() {
        this.fechaPostulacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoPostulacion.PENDIENTE;
        }
    }
}
