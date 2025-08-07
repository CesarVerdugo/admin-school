package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "asignacion_materia_grupo")
public class AsignacionMateriaGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @OneToMany(mappedBy = "asignacionMateriaGrupo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Actividad> actividades;
}