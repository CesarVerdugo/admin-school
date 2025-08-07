package com.verdugocode.administrador_escolar.dto.Matriculas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // Anotación de Lombok.
public class MatriculaRequestDTO {

    @NotNull(message = "El ID del estudiante no puede ser nulo")
    private Long estudianteId; // ID del Estudiante a matricular.

    @NotNull(message = "El ID del grupo no puede ser nulo")
    private Long grupoId; // ID del Grupo en el que se matricula.

    // La fecha de matrícula se podría generar en el servicio, no necesariamente en el request.
    // Si la vas a recibir del front-end, necesitarías un tipo de fecha aquí y validaciones.
    // Por simplicidad inicial, la generaremos en el servicio.

    @NotBlank(message = "El estado de la matrícula no puede estar vacío")
    @Size(min = 3, max = 50, message = "El estado debe tener entre 3 y 50 caracteres")
    private String estado; // Ej. "Activa", "Pendiente", "Completada".
}
