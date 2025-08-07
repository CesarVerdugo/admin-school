// Archivo: Materia.java
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
@Table(name = "materias")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private String description;

    // --- ¡CORRECCIÓN AQUÍ! ---
    // La relación ManyToOne debe ser con la entidad Profesor.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    // Relación OneToMany con AsignacionMateriaGrupo:
    // Una Materia puede estar en muchas asignaciones.
    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AsignacionMateriaGrupo> asignacionesMateria = new HashSet<>();
}