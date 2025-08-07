package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    List<Actividad> findByAsignacionMateriaGrupoId(Long asignacionMateriaGrupoId);

}
