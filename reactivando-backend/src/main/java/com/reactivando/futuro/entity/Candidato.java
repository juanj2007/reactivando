package com.reactivando.futuro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "candidatos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private Integer edad;

    @Column(name = "nivel_estudio", length = 100)
    private String nivelEstudio;

    @Column(length = 150)
    private String ocupacion;

    @Column(length = 200)
    private String direccion;

    @Column(length = 100)
    private String ciudad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String experiencia;

    @Column(columnDefinition = "TEXT")
    private String habilidades;

    @Column(name = "hoja_de_vida", length = 255)
    private String hojaDeVida;

    @Column(length = 255)
    private String foto;
}
