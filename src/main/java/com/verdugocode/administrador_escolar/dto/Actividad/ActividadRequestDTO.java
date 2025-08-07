package com.verdugocode.administrador_escolar.dto.Actividad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActividadRequestDTO {

    @NotBlank(message = "El título de la actividad no puede estar vacío")
    @Size(min = 3, max = 255, message = "El título debe tener entre 3 y 255 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "La fecha de entrega no puede ser nula")
    private LocalDate fechaEntrega;

    // Ya no se relaciona con el Grupo, sino con la AsignacionMateriaGrupo
    @NotNull(message = "El ID de la asignación de materia no puede ser nulo")
    private Long asignacionMateriaGrupoId;
}