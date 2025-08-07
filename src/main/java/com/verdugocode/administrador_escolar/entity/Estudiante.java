package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String numeroIdentificacion;

    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;

    @Column(unique = true)
    private String emailPersonal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User usuario;

    // Relación OneToMany con Matricula
    // Un estudiante tiene muchas matrículas
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Matricula> matriculas = new HashSet<>();

    // Métodos para conveniencia
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }
}