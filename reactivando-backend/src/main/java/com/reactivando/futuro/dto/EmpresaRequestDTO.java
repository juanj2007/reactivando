package com.reactivando.futuro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaRequestDTO {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre de la empresa debe tener entre 2 y 150 caracteres")
    private String nombreEmpresa;

    @NotBlank(message = "El NIT es obligatorio")
    @Size(min = 5, max = 50, message = "El NIT debe tener entre 5 y 50 caracteres")
    private String nit;

    private String descripcion;

    @Size(max = 200, message = "La dirección no debe exceder 200 caracteres")
    private String direccion;

    @Size(max = 100, message = "La ciudad no debe exceder 100 caracteres")
    private String ciudad;

    @Size(max = 20, message = "El teléfono no debe exceder 20 caracteres")
    private String telefono;

    @Email(message = "Debe proporcionar un correo corporativo válido")
    private String correoCorporativo;

    private Integer numeroEmpleados;
    private String sitioWeb;
    private Double latitud;
    private Double longitud;
}
