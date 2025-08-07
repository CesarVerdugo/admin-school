package com.verdugocode.administrador_escolar.dto.Estudiantes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EstudianteRequestDTO {
    @NotBlank(message = "El nombre del estudiante no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido del estudiante no puede estar vacío")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El número de identificación no puede estar vacío")
    @Size(min = 5, max = 50, message = "El número de identificación debe tener entre 5 y 50 caracteres")
    private String numeroIdentificacion;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    private String direccion; // Opcional

    private String telefono; // Opcional

    @Email(message = "Formato de email personal inválido")
    private String emailPersonal; // Email personal (opcional, pero si se envía, debe ser válido)

    // Necesitamos el ID del usuario existente o los datos para crear uno nuevo
    // Para simplificar, asumiremos que el usuario ya existe y se le asignará el rol de estudiante
    @NotNull(message = "El ID del usuario asociado no puede ser nulo")
    private Long userId;
}
