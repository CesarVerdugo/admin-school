package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.User.UserRequestDTO;
import com.verdugocode.administrador_escolar.dto.User.UserResponseDTO;
import com.verdugocode.administrador_escolar.entity.Role;
import com.verdugocode.administrador_escolar.entity.User;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.RoleRepository;
import com.verdugocode.administrador_escolar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections; // Para Collections.singletonList
import java.util.HashSet;    // Para HashSet
import java.util.List;
import java.util.Optional;
import java.util.Set;      // Para Set
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User convertToEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword());
        user.setEmail(userRequestDTO.getEmail());
        user.setEnabled(userRequestDTO.isEnabled());

        // Busca el objeto Role por su nombre
        Role role = roleRepository.findByName(userRequestDTO.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Rol", "Name", userRequestDTO.getRoleName()));

        // Asigna el objeto Role al conjunto de roles de la entidad User
        user.getRoles().add(role); // ¡CORRECCIÓN AQUÍ!
        return user;
    }

    // Convierte una entidad User a un UserResponseDTO
    private UserResponseDTO convertToDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        // Mapea el Set de Role a un Set de nombres de String para el DTO
        dto.setRoleNames(user.getRoles().stream() // ¡CORRECCIÓN AQUÍ!
                .map(Role::getName)
                .collect(Collectors.toSet()));
        dto.setEnabled(user.isEnabled());
        return dto;
    }
    // --- Fin Métodos de Mapeo ---


    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new ResourceAlreadyExistsException("Usuario", "nombre de usuario", userRequestDTO.getUsername());
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Usuario", "email", userRequestDTO.getEmail());
        }

        User user = convertToEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDetailsDTO) {
        return userRepository.findById(id).map(existingUser -> {
            if (!existingUser.getUsername().equals(userDetailsDTO.getUsername()) && userRepository.existsByUsername(userDetailsDTO.getUsername())) {
                throw new ResourceAlreadyExistsException("Usuario", "nombre de usuario", userDetailsDTO.getUsername());
            }
            if (!existingUser.getEmail().equals(userDetailsDTO.getEmail()) && userRepository.existsByEmail(userDetailsDTO.getEmail())) {
                throw new ResourceAlreadyExistsException("Usuario", "email", userDetailsDTO.getEmail());
            }

            existingUser.setUsername(userDetailsDTO.getUsername());
            existingUser.setEmail(userDetailsDTO.getEmail());

            if (userDetailsDTO.getPassword() != null && !userDetailsDTO.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDetailsDTO.getPassword()));
            }

            // Busca el nuevo rol por nombre
            Role newRole = roleRepository.findByName(userDetailsDTO.getRoleName())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol", "Name", userDetailsDTO.getRoleName()));

            // Limpia los roles existentes y añade solo el nuevo rol
            existingUser.getRoles().clear(); // Limpia el Set de roles actual
            existingUser.getRoles().add(newRole); // Añade el nuevo rol al Set

            existingUser.setEnabled(userDetailsDTO.isEnabled());

            User updatedUser = userRepository.save(existingUser);
            return convertToDto(updatedUser);
        }).orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "id", id);
        }
        userRepository.deleteById(id);
    }
}