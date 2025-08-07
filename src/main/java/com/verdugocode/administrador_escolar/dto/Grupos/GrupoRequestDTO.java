// Archivo: GrupoRequestDTO.java
package com.verdugocode.administrador_escolar.dto.Grupos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GrupoRequestDTO {

    @NotBlank(message = "El nombre del grupo no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombreGrupo;

    @NotNull(message = "El grado no puede ser nulo")
    private Integer grado;

    @NotBlank(message = "La jornada no puede estar vacía")
    private String jornada; // Ej: "Mañana", "Tarde"

    // Nota: El profesor y las materias se gestionan a través de la entidad AsignacionMateriaGrupo
}