package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository // Marca esta interfaz como un componente de repositorio de Spring.
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    // JpaRepository<TipoDeEntidad, TipoDeIdDeEntidad> proporciona métodos CRUD básicos.

    /**
     * Verifica si ya existe una matrícula para un estudiante en un grupo específico.
     * Esto es fundamental para hacer cumplir la restricción UNIQUE en la entidad Matricula.
     * @param estudianteId El ID del estudiante.
     * @param grupoId El ID del grupo.
     * @return true si ya existe una matrícula con esa combinación, false en caso contrario.
     */
    boolean existsByEstudianteIdAndGrupoId(Long estudianteId, Long grupoId);

    /**
     * Busca matrículas por el ID de un estudiante.
     * @param estudianteId El ID del estudiante.
     * @return Una lista de matrículas asociadas a ese estudiante.
     */
    List<Matricula> findByEstudianteId(Long estudianteId);

    /**
     * Busca matrículas por el ID de un grupo.
     * @param grupoId El ID del grupo.
     * @return Una lista de matrículas asociadas a ese grupo.
     */
    List<Matricula> findByGrupoId(Long grupoId);
}