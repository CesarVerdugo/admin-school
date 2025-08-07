package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fechaEntrega;

    // Relación ManyToOne con AsignacionMateriaGrupo:
    // Una actividad pertenece a una única asignación de materia.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignacion_materia_grupo_id", nullable = false)
    private AsignacionMateriaGrupo asignacionMateriaGrupo;

}