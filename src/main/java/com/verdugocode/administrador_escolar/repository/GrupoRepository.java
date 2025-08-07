package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Marca esta interfaz como un componente de repositorio de Spring.
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    // JpaRepository<TipoDeEntidad, TipoDeIdDeEntidad> proporciona métodos CRUD básicos.

    /**
     * Verifica si ya existe un grupo con el nombre especificado.
     * Útil para validar la unicidad del nombre del grupo si es un requisito.
     * @param nombreGrupo El nombre del grupo a verificar.
     * @return true si ya existe un grupo con ese nombre, false en caso contrario.
     */
    boolean existsByNombreGrupo(String nombreGrupo);


    }