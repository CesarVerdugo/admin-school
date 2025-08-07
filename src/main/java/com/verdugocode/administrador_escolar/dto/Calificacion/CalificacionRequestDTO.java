package com.verdugocode.administrador_escolar.dto.Calificacion;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalificacionRequestDTO {

    @NotNull(message = "El ID de la actividad no puede ser nulo")
    private Long actividadId;

    @NotNull(message = "El ID del estudiante no puede ser nulo")
    private Long estudianteId;

    @NotNull(message = "El valor de la calificación no puede ser nulo")
    private Double valor;

    private String comentarios;
}