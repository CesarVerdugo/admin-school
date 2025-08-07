package com.verdugocode.administrador_escolar.dto.Asignacion;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignacionRequestDTO {

    @NotNull(message = "El ID del grupo no puede ser nulo")
    private Long grupoId;

    @NotNull(message = "El ID de la materia no puede ser nulo")
    private Long materiaId;

    @NotNull(message = "El ID del profesor no puede ser nulo")
    private Long profesorId;

    @NotNull(message = "El ID del periodo académico no puede ser nulo")
    private Long periodoAcademicoId;
}