package com.verdugocode.administrador_escolar.service;


import com.verdugocode.administrador_escolar.entity.Role;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;

import com.verdugocode.administrador_escolar.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    // Constructor (ya existente)
    @Autowired // Opcional en Spring Boot si es el único constructor
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository; // Aquí se inicializa la variable 'final'
    }

    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id) {
        // En el servicio, a menudo quieres que este método lance una excepción si no lo encuentra,
        // para que el controlador pueda manejar el 404.
        // Pero si el servicio se usa internamente y manejará el Optional, está bien.
        // Por ahora, lo dejo como Optional, y el controlador lo manejará.
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public Role createRole(Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Rol", "nombre", role.getName()); // Usar ResourceAlreadyExistsException
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(Long id, Role roleDetails) {
        return roleRepository.findById(id).map(existingRole -> {
            // Si el nombre del rol cambia, verificar unicidad
            if (!existingRole.getName().equals(roleDetails.getName()) && roleRepository.findByName(roleDetails.getName()).isPresent()) {
                throw new ResourceAlreadyExistsException("Rol", "nombre", roleDetails.getName());
            }

            existingRole.setName(roleDetails.getName());
            existingRole.setDescription(roleDetails.getDescription());
            return roleRepository.save(existingRole);
        }).orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id)); // Usar ResourceNotFoundException
    }

    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rol", "id", id); // Usar ResourceNotFoundException
        }
        // CONSIDERACIÓN: Añadir lógica para verificar si hay usuarios asignados a este rol antes de eliminar
        // Si hay usuarios, podrías lanzar una 'BusinessRuleViolationException' o similar.
        roleRepository.deleteById(id);
    }

}
