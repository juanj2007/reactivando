package com.reactivando.futuro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "favoritos",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_candidato_favorito", columnNames = {"candidato_id", "vacante_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidato_id", nullable = false)
    private Candidato candidato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacante_id", nullable = false)
    private Vacante vacante;

    @Column(name = "fecha_agregado", nullable = false, updatable = false)
    private LocalDateTime fechaAgregado;

    @PrePersist
    protected void onCreate() {
        this.fechaAgregado = LocalDateTime.now();
    }
}
