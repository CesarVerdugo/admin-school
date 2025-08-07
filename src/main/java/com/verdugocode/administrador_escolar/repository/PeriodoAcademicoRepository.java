package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Long> {

    // Método para buscar un periodo por su nombre, útil para validaciones de unicidad.
    Optional<PeriodoAcademico> findByNombre(String nombre);

    // Método para verificar si un periodo con un nombre específico ya existe.
    boolean existsByNombre(String nombre);
}