// Archivo: MateriaResponseDTO.java
package com.verdugocode.administrador_escolar.dto.Materias;

import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionResponseDTO;
import com.verdugocode.administrador_escolar.dto.Profesor.ProfesorResponseDTO;
import lombok.Data;
import java.util.Set;

@Data
public class MateriaResponseDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private ProfesorResponseDTO profesorCoordinador; // DTO del profesor coordinador
    private Set<AsignacionResponseDTO> asignacionesMateria;
}