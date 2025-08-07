package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.User.UserRequestDTO;
import com.verdugocode.administrador_escolar.dto.User.UserResponseDTO;
import com.verdugocode.administrador_escolar.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() { // Retorna lista de DTOs
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) { // Retorna un DTO
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) { // Recibe DTO de Request
        try {
            UserResponseDTO newUser = userService.createUser(userRequestDTO); // Servicio devuelve DTO de Response
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Aquí podrías retornar un objeto de error JSON más estructurado si lo deseas.
            // Por ahora, simplemente un Bad Request.
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userDetailsDTO) { // Recibe DTO de Request
        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userDetailsDTO); // Servicio devuelve DTO de Response
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
