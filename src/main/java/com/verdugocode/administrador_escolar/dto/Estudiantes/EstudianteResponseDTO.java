package com.verdugocode.administrador_escolar.dto.Estudiantes;

import lombok.Data;

import java.time.LocalDate;
@Data
public class EstudianteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String numeroIdentificacion;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String emailPersonal;

    // Detalles del usuario asociado
    private Long userId;
    private String username;
    private String userEmail;
}
