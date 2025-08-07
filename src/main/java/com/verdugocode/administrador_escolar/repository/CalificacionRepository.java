package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    boolean existsByEstudianteIdAndActividadId(Long estudianteId, Long actividadId);
    Optional<Calificacion> findByEstudianteIdAndActividadId(Long estudianteId, Long actividadId);

}