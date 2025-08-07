package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MateriaRepository extends JpaRepository<Materia,Long> {
    Optional<Materia> findByName(String name);
    Optional<Materia> findByCode(String code);

    Boolean existsByName(String name);
    Boolean existsByCode(String code);

}
