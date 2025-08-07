package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.AsignacionMateriaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionMateriaGrupoRepository extends JpaRepository<AsignacionMateriaGrupo, Long> {

    // Método para verificar si ya existe una asignación con la misma combinación
    // de grupo, materia, profesor y periodo académico.
    boolean existsByGrupoIdAndMateriaIdAndProfesorIdAndPeriodoAcademicoId(
            Long grupoId, Long materiaId, Long profesorId, Long periodoAcademicoId);

    // Método para encontrar todas las asignaciones de un grupo específico.
    List<AsignacionMateriaGrupo> findByGrupoId(Long grupoId);

    // Método para encontrar todas las asignaciones de un profesor específico.
    List<AsignacionMateriaGrupo> findByProfesorId(Long profesorId);

    List<AsignacionMateriaGrupo> findByMateriaId(Long materiaId);

    Optional<AsignacionMateriaGrupo> findByProfesorIdAndMateriaIdAndGrupoIdAndPeriodoAcademicoId(
            Long profesorId,
            Long materiaId,
            Long grupoId,
            Long periodoAcademicoId);

    Optional<AsignacionMateriaGrupo> findByProfesorIdAndMateriaIdAndGrupoId(
            Long profesorId,
            Long materiaId,
            Long grupoId);
}