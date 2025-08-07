package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Profesor.ProfesorRequestDTO;
import com.verdugocode.administrador_escolar.dto.Profesor.ProfesorResponseDTO;
import com.verdugocode.administrador_escolar.entity.Profesor;
import com.verdugocode.administrador_escolar.entity.Role;
import com.verdugocode.administrador_escolar.entity.User;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.ProfesorRepository;
import com.verdugocode.administrador_escolar.repository.RoleRepository;
import com.verdugocode.administrador_escolar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public ProfesorService(ProfesorRepository profesorRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.profesorRepository = profesorRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // --- Métodos de Mapeo (Conversores) ---

    private ProfesorResponseDTO convertToDTO(Profesor profesor) {
        ProfesorResponseDTO dto = new ProfesorResponseDTO();
        dto.setId(profesor.getId());
        dto.setUserId(profesor.getUser().getId());
        dto.setNombre(profesor.getNombre());
        dto.setApellido(profesor.getApellido());
        dto.setNumeroIdentificacion(profesor.getNumeroIdentificacion());
        dto.setFechaNacimiento(profesor.getFechaNacimiento());
        dto.setDireccion(profesor.getDireccion());
        dto.setTelefono(profesor.getTelefono());
        dto.setEmailPersonal(profesor.getEmailPersonal());
        dto.setNumeroEmpleado(profesor.getNumeroEmpleado());
        dto.setTituloAcademico(profesor.getTituloAcademico());
        dto.setUsername(profesor.getUser().getUsername());
        dto.setEmailLogin(profesor.getUser().getEmail());
        return dto;
    }

    private Profesor convertToEntity(ProfesorRequestDTO requestDTO) {
        Profesor profesor = new Profesor();
        profesor.setNombre(requestDTO.getNombre());
        profesor.setApellido(requestDTO.getApellido());
        profesor.setNumeroIdentificacion(requestDTO.getNumeroIdentificacion());
        profesor.setFechaNacimiento(requestDTO.getFechaNacimiento());
        profesor.setDireccion(requestDTO.getDireccion());
        profesor.setTelefono(requestDTO.getTelefono());
        profesor.setEmailPersonal(requestDTO.getEmailPersonal());
        profesor.setNumeroEmpleado(requestDTO.getNumeroEmpleado());
        profesor.setTituloAcademico(requestDTO.getTituloAcademico());
        return profesor;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---

    @Transactional(readOnly = true)
    public List<ProfesorResponseDTO> getAllProfesores() {
        return profesorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfesorResponseDTO getProfesorById(Long id) {
        return profesorRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "ID", id));
    }

    @Transactional
    public ProfesorResponseDTO createProfesor(ProfesorRequestDTO requestDTO) {
        // 1. Validar que el usuario asociado exista
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", requestDTO.getUserId()));

        // 2. Validar que el usuario no esté asociado a otro profesor
        if (profesorRepository.existsByUserId(user.getId())) {
            throw new ResourceAlreadyExistsException("Profesor", "ID de Usuario", user.getId());
        }

        // 3. Asignar el rol de PROFESOR al usuario
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_PROFESOR");
        if (roleOptional.isPresent()) {
            user.setRoles(Collections.singleton(roleOptional.get()));
        } else {
            throw new ResourceNotFoundException("Rol", "nombre", "ROLE_PROFESOR");
        }

        // 4. Validar unicidad del número de empleado
        if (profesorRepository.existsByNumeroEmpleado(requestDTO.getNumeroEmpleado())) {
            throw new ResourceAlreadyExistsException("Profesor", "Número de Empleado", requestDTO.getNumeroEmpleado());
        }

        // 5. Crear la entidad Profesor y guardar
        Profesor profesor = convertToEntity(requestDTO);
        profesor.setUser(user); // Asignar el usuario al profesor
        Profesor savedProfesor = profesorRepository.save(profesor);

        // 6. Retornar el DTO de respuesta
        return convertToDTO(savedProfesor);
    }

    @Transactional
    public ProfesorResponseDTO updateProfesor(Long id, ProfesorRequestDTO requestDTO) {
        return profesorRepository.findById(id).map(existingProfesor -> {
            // Validar que el usuario asociado exista (podría ser el mismo o uno nuevo)
            User newUser = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", requestDTO.getUserId()));

            // Si el usuario cambia, validar que el nuevo no esté ya asociado a otro profesor
            if (!existingProfesor.getUser().getId().equals(newUser.getId())) {
                if (profesorRepository.existsByUserId(newUser.getId())) {
                    throw new ResourceAlreadyExistsException("Profesor", "ID de Usuario", newUser.getId());
                }
            }

            // Actualizar el rol del nuevo usuario si es necesario
            Optional<Role> roleOptional = roleRepository.findByName("ROLE_PROFESOR");
            if (roleOptional.isPresent()) {
                newUser.setRoles(Collections.singleton(roleOptional.get()));
            } else {
                throw new ResourceNotFoundException("Rol", "nombre", "ROLE_PROFESOR");
            }
            existingProfesor.setUser(newUser);

            // Validar unicidad del número de empleado si cambia
            if (!existingProfesor.getNumeroEmpleado().equals(requestDTO.getNumeroEmpleado()) &&
                    profesorRepository.existsByNumeroEmpleado(requestDTO.getNumeroEmpleado())) {
                throw new ResourceAlreadyExistsException("Profesor", "Número de Empleado", requestDTO.getNumeroEmpleado());
            }

            // Actualizar los campos del profesor
            existingProfesor.setNombre(requestDTO.getNombre());
            existingProfesor.setApellido(requestDTO.getApellido());
            existingProfesor.setNumeroIdentificacion(requestDTO.getNumeroIdentificacion());
            existingProfesor.setFechaNacimiento(requestDTO.getFechaNacimiento());
            existingProfesor.setDireccion(requestDTO.getDireccion());
            existingProfesor.setTelefono(requestDTO.getTelefono());
            existingProfesor.setEmailPersonal(requestDTO.getEmailPersonal());
            existingProfesor.setNumeroEmpleado(requestDTO.getNumeroEmpleado());
            existingProfesor.setTituloAcademico(requestDTO.getTituloAcademico());

            // Guardar los cambios
            Profesor updatedProfesor = profesorRepository.save(existingProfesor);
            return convertToDTO(updatedProfesor);

        }).orElseThrow(() -> new ResourceNotFoundException("Profesor", "ID", id));
    }

    @Transactional
    public void deleteProfesor(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profesor", "ID", id);
        }
        // Nota: La eliminación en cascada (CascadeType.ALL) en la entidad User
        // se encargará de eliminar el profesor si eliminas el usuario, pero
        // aquí es mejor eliminar directamente el profesor para no afectar al usuario.
        profesorRepository.deleteById(id);
    }
}