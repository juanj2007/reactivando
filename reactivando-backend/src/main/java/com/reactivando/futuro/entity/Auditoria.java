package com.reactivando.futuro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(length = 50)
    private String ip;

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }
}
