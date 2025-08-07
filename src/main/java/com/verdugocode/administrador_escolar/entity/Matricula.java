package com.verdugocode.administrador_escolar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime; // Para la fecha y hora de la matrícula

@Data // Anotación de Lombok para generar getters, setters, toString, equals y hashCode automáticamente.
@NoArgsConstructor // Anotación de Lombok para generar un constructor sin argumentos.
@AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los argumentos.
@Entity // Indica que esta clase es una entidad JPA y se mapeará a una tabla de base de datos.
@Table(name = "matriculas", uniqueConstraints = {
        // Esta restricción asegura que un mismo estudiante solo pueda estar
        // matriculado una vez en un grupo específico.
        @UniqueConstraint(columnNames = {"estudiante_id", "grupo_id"})
})
public class Matricula {

    @Id // Marca el campo 'id' como la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática de valores para la clave primaria.
    private Long id;

    // Relación Many-to-One con Estudiante:
    // Un estudiante puede tener muchas matrículas.
    @ManyToOne(fetch = FetchType.LAZY) // Carga perezosa del estudiante.
    @JoinColumn(name = "estudiante_id", nullable = false) // Columna de clave foránea.
    private Estudiante estudiante;

    // Relación Many-to-One con Grupo:
    // Un grupo puede tener muchas matrículas (muchos estudiantes en ese grupo).
    @ManyToOne(fetch = FetchType.LAZY) // Carga perezosa del grupo.
    @JoinColumn(name = "grupo_id", nullable = false) // Columna de clave foránea.
    private Grupo grupo;

    @Column(nullable = false)
    private LocalDateTime fechaMatricula; // La fecha y hora en que se realizó la matrícula.

    @Column(nullable = false)
    private String estado; // Estado de la matrícula (ej. "Activa", "Inactiva", "Completada", "Cancelada").

    // Puedes añadir más campos si lo necesitas, como:
    // private String notasAdicionales;
    // private Boolean pagado;
}
