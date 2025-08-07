package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    // Buscar un profesor por el ID de su usuario asociado
    Optional<Profesor> findByUserId(Long userId);

    // Verificar si ya existe un profesor para un ID de usuario dado
    boolean existsByUserId(Long userId);

    // Verificar si ya existe un profesor con un número de empleado
    boolean existsByNumeroEmpleado(String numeroEmpleado);
}