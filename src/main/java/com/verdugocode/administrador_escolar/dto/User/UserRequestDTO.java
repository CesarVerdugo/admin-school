package com.verdugocode.administrador_escolar.dto.User;

import com.verdugocode.administrador_escolar.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    // La contraseña es obligatoria al crear, pero puede ser opcional al actualizar
    // Por simplicidad, la hacemos obligatoria aquí y manejaremos la lógica en el servicio para actualizaciones.
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotNull(message = "El rol no puede ser nulo")
    private String roleName; // Recibimos el nombre del rol (ej., "ROLE_PROFESOR")


    private boolean enabled = true; // Por defecto, el usuario está habilitado
}
