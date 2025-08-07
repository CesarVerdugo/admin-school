package com.verdugocode.administrador_escolar.dto.Actividad;

import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionResponseDTO;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ActividadResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaEntrega;
    private AsignacionResponseDTO asignacionMateriaGrupo; // Detalles de la asignación
    private Long asignacionMateriaGrupoId;
}