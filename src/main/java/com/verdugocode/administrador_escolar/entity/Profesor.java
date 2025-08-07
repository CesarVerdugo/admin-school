package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "profesor")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String numeroIdentificacion;

    @Column
    private LocalDate fechaNacimiento;

    @Column
    private String direccion;

    @Column
    private String telefono;

    @Column
    private String emailPersonal;

    @Column(unique = true, nullable = false)
    private String numeroEmpleado;

    private String tituloAcademico;
}