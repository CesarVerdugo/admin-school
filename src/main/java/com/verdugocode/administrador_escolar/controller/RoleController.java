package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.entity.Role;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "id", id)); // Lanza si no se encuentra
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role newRole = roleService.createRole(role); // Las excepciones se propagarán
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @Valid @RequestBody Role roleDetails) {
        Role updatedRole = roleService.updateRole(id, roleDetails); // Las excepciones se propagarán
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id); // La excepción se propagará
        return ResponseEntity.noContent().build();
    }
}
