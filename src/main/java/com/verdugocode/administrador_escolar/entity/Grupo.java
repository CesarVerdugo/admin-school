package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombreGrupo;

    @Column(nullable = false)
    private Integer grado;

    @Column(nullable = false)
    private String jornada;

    // Relación OneToMany con AsignacionMateriaGrupo:
    // Un Grupo tiene muchas asignaciones de materia.
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AsignacionMateriaGrupo> asignacionesMateria = new HashSet<>();

    // AHORA: Relación OneToMany con Matricula:
    // Un grupo tiene muchas matrículas. Esta es la forma correcta de relacionarlo con los estudiantes.
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Matricula> matriculas = new HashSet<>();
}