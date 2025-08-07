package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Estudiante;
import com.verdugocode.administrador_escolar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByNumeroIdentificacion(String numero);
    Optional<Estudiante> findByUsuarioId(Long id);
    Optional<Estudiante> findByUsuario(User user);

    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
}
