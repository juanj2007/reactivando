package com.reactivando.futuro.dto.auth;

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
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre / razón social debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido / contacto es obligatorio")
    @Size(min = 2, max = 80, message = "El apellido / representante legal debe tener entre 2 y 80 caracteres")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Size(min = 5, max = 80, message = "El correo electrónico debe tener entre 5 y 80 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 30, message = "La contraseña debe tener entre 6 y 30 caracteres")
    private String password;

    @NotBlank(message = "El teléfono de contacto es obligatorio")
    @Size(min = 7, max = 15, message = "El teléfono debe tener entre 7 y 15 caracteres")
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}
