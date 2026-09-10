package com.reactivando.futuro.dto;

import com.reactivando.futuro.entity.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Size(max = 150, message = "El correo no puede exceder los 150 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}
