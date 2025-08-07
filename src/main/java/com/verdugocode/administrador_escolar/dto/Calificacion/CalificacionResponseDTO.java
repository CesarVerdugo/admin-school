package com.verdugocode.administrador_escolar.dto.Calificacion;

import com.verdugocode.administrador_escolar.dto.Actividad.ActividadResponseDTO;
import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteResponseDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalificacionResponseDTO {

    private Long id;
    private Double valor;
    private String comentarios;
    private LocalDate fechaCalificacion;
    private EstudianteResponseDTO estudiante;
    private ActividadResponseDTO actividad;
}