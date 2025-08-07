package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteResponseDTO;
import com.verdugocode.administrador_escolar.dto.Grupos.GrupoResponseDTO;
import com.verdugocode.administrador_escolar.dto.Matriculas.MatriculaRequestDTO;
import com.verdugocode.administrador_escolar.dto.Matriculas.MatriculaResponseDTO;
import com.verdugocode.administrador_escolar.entity.Estudiante;
import com.verdugocode.administrador_escolar.entity.Grupo;
import com.verdugocode.administrador_escolar.entity.Matricula;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.EstudianteRepository;
import com.verdugocode.administrador_escolar.repository.GrupoRepository;
import com.verdugocode.administrador_escolar.repository.MatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final GrupoRepository grupoRepository;

    @Autowired
    public MatriculaService(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository, GrupoRepository grupoRepository) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.grupoRepository = grupoRepository;
    }

    // --- Métodos de Mapeo (Conversores) ---
    private MatriculaResponseDTO convertToDTO(Matricula matricula) {
        MatriculaResponseDTO dto = new MatriculaResponseDTO();
        dto.setId(matricula.getId());
        dto.setFechaMatricula(matricula.getFechaMatricula());
        dto.setEstado(matricula.getEstado());

        // Mapeamos las entidades a DTOs directamente aquí para romper el ciclo
        dto.setEstudiante(convertEstudianteToDTO(matricula.getEstudiante()));
        dto.setGrupo(convertGrupoToDTO(matricula.getGrupo()));

        return dto;
    }

    private Matricula convertToEntity(MatriculaRequestDTO requestDTO) {
        Estudiante estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "ID", requestDTO.getEstudianteId()));

        Grupo grupo = grupoRepository.findById(requestDTO.getGrupoId())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "ID", requestDTO.getGrupoId()));

        Matricula matricula = new Matricula();
        matricula.setEstudiante(estudiante);
        matricula.setGrupo(grupo);
        matricula.setFechaMatricula(LocalDateTime.now());
        matricula.setEstado(requestDTO.getEstado());
        return matricula;
    }

    private EstudianteResponseDTO convertEstudianteToDTO(Estudiante estudiante) {
        EstudianteResponseDTO dto = new EstudianteResponseDTO();
        dto.setId(estudiante.getId());
        dto.setNombre(estudiante.getNombre());
        dto.setApellido(estudiante.getApellido());
        dto.setNumeroIdentificacion(estudiante.getNumeroIdentificacion());
        dto.setFechaNacimiento(estudiante.getFechaNacimiento());
        dto.setDireccion(estudiante.getDireccion());
        dto.setTelefono(estudiante.getTelefono());
        dto.setEmailPersonal(estudiante.getEmailPersonal());
        dto.setUsername(estudiante.getUsuario().getUsername());
        dto.setUserEmail(estudiante.getUsuario().getEmail());
        return dto;
    }

    private GrupoResponseDTO convertGrupoToDTO(Grupo grupo) {
        GrupoResponseDTO dto = new GrupoResponseDTO();
        dto.setId(grupo.getId());
        dto.setNombreGrupo(grupo.getNombreGrupo());
        dto.setGrado(grupo.getGrado());
        dto.setJornada(grupo.getJornada());
        // No necesitamos asignaciones aquí para romper el ciclo, ya que el servicio
        // de Grupo es el que realmente las maneja. Si se necesitan, se deben buscar
        // en otro endpoint.
        return dto;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> getAllMatriculas() {
        return matriculaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MatriculaResponseDTO getMatriculaById(Long id) {
        return matriculaRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Matrícula", "ID", id));
    }

    @Transactional
    public MatriculaResponseDTO createMatricula(MatriculaRequestDTO requestDTO) {
        if (matriculaRepository.existsByEstudianteIdAndGrupoId(requestDTO.getEstudianteId(), requestDTO.getGrupoId())) {
            throw new ResourceAlreadyExistsException("Matrícula", "combinación", "Estudiante ID: " + requestDTO.getEstudianteId() + ", Grupo ID: " + requestDTO.getGrupoId());
        }

        Matricula matricula = convertToEntity(requestDTO);
        Matricula savedMatricula = matriculaRepository.save(matricula);
        return convertToDTO(savedMatricula);
    }

    @Transactional
    public MatriculaResponseDTO updateMatricula(Long id, MatriculaRequestDTO requestDTO) {
        return matriculaRepository.findById(id).map(existingMatricula -> {
            Estudiante estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "ID", requestDTO.getEstudianteId()));

            Grupo grupo = grupoRepository.findById(requestDTO.getGrupoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo", "ID", requestDTO.getGrupoId()));

            existingMatricula.setEstudiante(estudiante);
            existingMatricula.setGrupo(grupo);
            existingMatricula.setEstado(requestDTO.getEstado());

            Matricula updatedMatricula = matriculaRepository.save(existingMatricula);
            return convertToDTO(updatedMatricula);
        }).orElseThrow(() -> new ResourceNotFoundException("Matrícula", "ID", id));
    }

    @Transactional
    public void deleteMatricula(Long id) {
        if (!matriculaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Matrícula", "ID", id);
        }
        matriculaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> getEstudiantesByGrupoId(Long grupoId) {
        grupoRepository.findById(grupoId)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "ID", grupoId));

        return matriculaRepository.findByGrupoId(grupoId).stream()
                .map(Matricula::getEstudiante)
                .map(this::convertEstudianteToDTO)
                .collect(Collectors.toList());
    }
}