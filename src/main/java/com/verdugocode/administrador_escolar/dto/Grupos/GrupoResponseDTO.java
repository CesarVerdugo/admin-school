// Archivo: GrupoResponseDTO.java
package com.verdugocode.administrador_escolar.dto.Grupos;

import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionResponseDTO;
import com.verdugocode.administrador_escolar.dto.User.UserResponseDTO;
import lombok.Data;

import java.util.Set;

@Data
public class GrupoResponseDTO {

    private Long id;
    private String nombreGrupo;
    private Integer grado;
    private String jornada;
    // Ahora retornamos una lista de asignaciones de materia para ver qué se enseña en el grupo
    private Set<AsignacionResponseDTO> asignacionesMateria;
}