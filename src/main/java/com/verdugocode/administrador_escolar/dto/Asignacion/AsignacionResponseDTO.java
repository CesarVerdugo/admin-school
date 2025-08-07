package com.verdugocode.administrador_escolar.dto.Asignacion;

import lombok.Data;

@Data
public class AsignacionResponseDTO {

    private Long id;
    private Long grupoId;
    private String nombreGrupo;
    private Long materiaId;
    private String nombreMateria;
    private Long profesorId;
    private String nombreProfesor;
    private Long periodoAcademicoId;
    private String nombrePeriodoAcademico;
}