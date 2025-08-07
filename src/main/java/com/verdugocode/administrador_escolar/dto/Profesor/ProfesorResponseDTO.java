package com.verdugocode.administrador_escolar.dto.Profesor;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfesorResponseDTO {

    private Long id;
    private Long userId; // ID del usuario relacionado
    private String nombre;
    private String apellido;
    private String numeroIdentificacion;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private String emailPersonal;
    private String numeroEmpleado;
    private String tituloAcademico;
    private String username; // Campo del usuario relacionado
    private String emailLogin; // Campo del usuario relacionado
}