package com.verdugocode.administrador_escolar.dto.Matriculas;

import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteResponseDTO;
import com.verdugocode.administrador_escolar.dto.Grupos.GrupoResponseDTO; // Necesitas un DTO del grupo simplificado
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MatriculaResponseDTO {

    private Long id;
    private LocalDateTime fechaMatricula;
    private String estado;
    private EstudianteResponseDTO estudiante;
    private GrupoResponseDTO grupo;
}