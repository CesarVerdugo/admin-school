package com.verdugocode.administrador_escolar.dto.PeriodoAcademico;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PeriodoAcademicoResponseDTO {

    private Long id;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}