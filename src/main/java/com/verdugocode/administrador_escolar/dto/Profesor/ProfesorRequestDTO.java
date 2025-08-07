package com.verdugocode.administrador_escolar.dto.Profesor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfesorRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El número de identificación no puede estar vacío")
    @Size(min = 5, max = 20, message = "El número de identificación debe tener entre 5 y 20 caracteres")
    private String numeroIdentificacion;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    private String telefono; // Campo opcional
    private String emailPersonal; // Campo opcional, diferente al email de login

    @NotBlank(message = "El número de empleado no puede estar vacío")
    private String numeroEmpleado;

    private String tituloAcademico; // Campo opcional

    // Referencia al ID del usuario ya existente
    @NotNull(message = "El ID del usuario asociado no puede ser nulo")
    private Long userId;
}