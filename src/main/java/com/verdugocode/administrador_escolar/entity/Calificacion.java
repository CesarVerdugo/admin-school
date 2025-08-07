package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "calificaciones", uniqueConstraints = {
        // Un estudiante solo puede tener una calificación por actividad
        @UniqueConstraint(columnNames = {"estudiante_id", "actividad_id"})
})
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double valor;

    private String comentarios;

    @Column(nullable = false)
    private LocalDate fechaCalificacion;

    // Relación Many-to-One: Un estudiante tiene muchas calificaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    // Relación Many-to-One: Una actividad tiene muchas calificaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;
}