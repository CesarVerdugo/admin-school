// Archivo: MateriaRequestDTO.java
package com.verdugocode.administrador_escolar.dto.Materias;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MateriaRequestDTO {

    @NotBlank(message = "El nombre de la materia no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "El código de la materia no puede estar vacío")
    @Size(min = 2, max = 20, message = "El código debe tener entre 2 y 20 caracteres")
    private String code;

    @NotBlank(message = "La descripción de la materia no puede estar vacía")
    private String description;

    @NotNull(message = "El ID del profesor no puede ser nulo")
    private Long profesorId; // Este es el ID del profesor coordinador de la materia a nivel institucional.
}