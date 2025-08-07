// Archivo: MateriaService.java
package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Materias.MateriaRequestDTO;
import com.verdugocode.administrador_escolar.dto.Materias.MateriaResponseDTO;
import com.verdugocode.administrador_escolar.dto.Profesor.ProfesorResponseDTO; // Importar el DTO correcto
import com.verdugocode.administrador_escolar.entity.Materia;
import com.verdugocode.administrador_escolar.entity.Profesor;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.MateriaRepository;
import com.verdugocode.administrador_escolar.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;
    private final ProfesorRepository profesorRepository;
    private final AsignacionMateriaGrupoService asignacionMateriaGrupoService;

    @Autowired
    public MateriaService(MateriaRepository materiaRepository, ProfesorRepository profesorRepository, AsignacionMateriaGrupoService asignacionMateriaGrupoService) {
        this.materiaRepository = materiaRepository;
        this.profesorRepository = profesorRepository;
        this.asignacionMateriaGrupoService = asignacionMateriaGrupoService;
    }

    // --- Métodos de Mapeo (Conversores) ---
    private ProfesorResponseDTO convertProfesorToDTO(Profesor profesor) {
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

    private MateriaResponseDTO convertToDTO(Materia materia) {
        MateriaResponseDTO dto = new MateriaResponseDTO();
        dto.setId(materia.getId());
        dto.setName(materia.getName());
        dto.setCode(materia.getCode());
        dto.setDescription(materia.getDescription());
        dto.setProfesorCoordinador(convertProfesorToDTO(materia.getProfesor()));
        dto.setAsignacionesMateria(
                asignacionMateriaGrupoService.getAsignacionesByMateriaId(materia.getId()).stream()
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    private Materia convertToEntity(MateriaRequestDTO requestDTO) {
        Materia materia = new Materia();
        materia.setName(requestDTO.getName());
        materia.setCode(requestDTO.getCode());
        materia.setDescription(requestDTO.getDescription());
        Profesor profesor = profesorRepository.findById(requestDTO.getProfesorId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "ID", requestDTO.getProfesorId()));
        // --- ¡CORRECCIÓN AQUÍ! ---
        // Asignamos el objeto Profesor, no el objeto User.
        materia.setProfesor(profesor);
        return materia;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---
    @Transactional(readOnly = true)
    public List<MateriaResponseDTO> getAllMaterias() {
        return materiaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MateriaResponseDTO getMateriaById(Long id) {
        return materiaRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Materia", "ID", id));
    }

    @Transactional
    public MateriaResponseDTO createMateria(MateriaRequestDTO requestDTO) {
        if (materiaRepository.existsByCode(requestDTO.getCode())) {
            throw new ResourceAlreadyExistsException("Materia", "código", requestDTO.getCode());
        }
        Materia materia = convertToEntity(requestDTO);
        Materia savedMateria = materiaRepository.save(materia);
        return convertToDTO(savedMateria);
    }

    @Transactional
    public MateriaResponseDTO updateMateria(Long id, MateriaRequestDTO requestDTO) {
        return materiaRepository.findById(id).map(existingMateria -> {
            if (!existingMateria.getCode().equals(requestDTO.getCode()) &&
                    materiaRepository.existsByCode(requestDTO.getCode())) {
                throw new ResourceAlreadyExistsException("Materia", "código", requestDTO.getCode());
            }

            Profesor profesor = profesorRepository.findById(requestDTO.getProfesorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesor", "ID", requestDTO.getProfesorId()));

            existingMateria.setName(requestDTO.getName());
            existingMateria.setCode(requestDTO.getCode());
            existingMateria.setDescription(requestDTO.getDescription());
            // --- ¡CORRECCIÓN AQUÍ! ---
            // Asignamos el objeto Profesor, no el objeto User.
            existingMateria.setProfesor(profesor);

            Materia updatedMateria = materiaRepository.save(existingMateria);
            return convertToDTO(updatedMateria);
        }).orElseThrow(() -> new ResourceNotFoundException("Materia", "ID", id));
    }

    @Transactional
    public void deleteMateria(Long id) {
        if (!materiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Materia", "ID", id);
        }
        materiaRepository.deleteById(id);
    }
}