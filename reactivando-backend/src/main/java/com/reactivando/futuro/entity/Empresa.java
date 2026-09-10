package com.reactivando.futuro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "nombre_empresa", nullable = false, length = 150)
    private String nombreEmpresa;

    @Column(nullable = false, unique = true, length = 50)
    private String nit;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 200)
    private String direccion;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 20)
    private String telefono;

    @Column(name = "correo_corporativo", length = 150)
    private String correoCorporativo;

    @Column(name = "numero_empleados")
    private Integer numeroEmpleados;

    @Column(name = "sitio_web", length = 255)
    private String sitioWeb;

    private Double latitud;

    private Double longitud;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    @Builder.Default
    private Boolean estado = true;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = true;
        }
    }
}
