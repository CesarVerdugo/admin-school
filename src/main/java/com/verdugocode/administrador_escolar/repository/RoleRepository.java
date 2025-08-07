package com.verdugocode.administrador_escolar.repository;

import com.verdugocode.administrador_escolar.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name); // Para buscar un rol por su nombre (ej., "ROLE_RECTOR")

}
