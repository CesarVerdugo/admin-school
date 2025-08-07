package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionRequestDTO;
import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionResponseDTO;
import com.verdugocode.administrador_escolar.entity.*;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignacionMateriaGrupoService {

    private final AsignacionMateriaGrupoRepository asignacionRepository;
    private final GrupoRepository grupoRepository;
    private final MateriaRepository materiaRepository;
    private final ProfesorRepository profesorRepository;
    private final PeriodoAcademicoRepository periodoAcademicoRepository;

    @Autowired
    public AsignacionMateriaGrupoService(AsignacionMateriaGrupoRepository asignacionRepository,
                                         GrupoRepository grupoRepository,
                                         MateriaRepository materiaRepository,
                                         ProfesorRepository profesorRepository,
                                         PeriodoAcademicoRepository periodoAcademicoRepository) {
        this.asignacionRepository = asignacionRepository;
        this.grupoRepository = grupoRepository;
        this.materiaRepository = materiaRepository;
        this.profesorRepository = profesorRepository;
        this.periodoAcademicoRepository = periodoAcademicoRepository;
    }

    // --- Métodos de Mapeo (Conversores) ---
    private AsignacionResponseDTO convertToDTO(AsignacionMateriaGrupo asignacion) {
        AsignacionResponseDTO dto = new AsignacionResponseDTO();
        dto.setId(asignacion.getId());
        dto.setGrupoId(asignacion.getGrupo().getId());
        dto.setNombreGrupo(asignacion.getGrupo().getNombreGrupo());
        dto.setMateriaId(asignacion.getMateria().getId());
        dto.setNombreMateria(asignacion.getMateria().getName());
        dto.setProfesorId(asignacion.getProfesor().getId());
        dto.setNombreProfesor(asignacion.getProfesor().getNombre());
        dto.setPeriodoAcademicoId(asignacion.getPeriodoAcademico().getId());
        dto.setNombrePeriodoAcademico(asignacion.getPeriodoAcademico().getNombre());
        return dto;
    }

    private AsignacionMateriaGrupo convertToEntity(AsignacionRequestDTO requestDTO) {
        Grupo grupo = grupoRepository.findById(requestDTO.getGrupoId())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "ID", requestDTO.getGrupoId()));
        Materia materia = materiaRepository.findById(requestDTO.getMateriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Materia", "ID", requestDTO.getMateriaId()));
        Profesor profesor = profesorRepository.findById(requestDTO.getProfesorId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesor", "ID", requestDTO.getProfesorId()));
        PeriodoAcademico periodo = periodoAcademicoRepository.findById(requestDTO.getPeriodoAcademicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Periodo Académico", "ID", requestDTO.getPeriodoAcademicoId()));

        AsignacionMateriaGrupo asignacion = new AsignacionMateriaGrupo();
        asignacion.setGrupo(grupo);
        asignacion.setMateria(materia);
        asignacion.setProfesor(profesor);
        asignacion.setPeriodoAcademico(periodo);
        return asignacion;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---

    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> getAllAsignaciones() {
        return asignacionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsignacionResponseDTO getAsignacionById(Long id) {
        return asignacionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación de Materia", "ID", id));
    }

    @Transactional
    public AsignacionResponseDTO createAsignacion(AsignacionRequestDTO requestDTO) {
        // Validar unicidad de la combinación
        if (asignacionRepository.existsByGrupoIdAndMateriaIdAndProfesorIdAndPeriodoAcademicoId(
                requestDTO.getGrupoId(),
                requestDTO.getMateriaId(),
                requestDTO.getProfesorId(),
                requestDTO.getPeriodoAcademicoId()
        )) {
            throw new ResourceAlreadyExistsException("Asignación de Materia", "combinación",
                    "Grupo ID: " + requestDTO.getGrupoId() +
                            ", Materia ID: " + requestDTO.getMateriaId() +
                            ", Profesor ID: " + requestDTO.getProfesorId() +
                            ", Periodo Académico ID: " + requestDTO.getPeriodoAcademicoId());
        }

        AsignacionMateriaGrupo asignacion = convertToEntity(requestDTO);
        AsignacionMateriaGrupo savedAsignacion = asignacionRepository.save(asignacion);
        return convertToDTO(savedAsignacion);
    }

    @Transactional
    public AsignacionResponseDTO updateAsignacion(Long id, AsignacionRequestDTO requestDTO) {
        return asignacionRepository.findById(id).map(existingAsignacion -> {
            // Validar unicidad si la combinación de IDs cambia
            boolean changed = !(existingAsignacion.getGrupo().getId().equals(requestDTO.getGrupoId()) &&
                    existingAsignacion.getMateria().getId().equals(requestDTO.getMateriaId()) &&
                    existingAsignacion.getProfesor().getId().equals(requestDTO.getProfesorId()) &&
                    existingAsignacion.getPeriodoAcademico().getId().equals(requestDTO.getPeriodoAcademicoId()));

            if (changed && asignacionRepository.existsByGrupoIdAndMateriaIdAndProfesorIdAndPeriodoAcademicoId(
                    requestDTO.getGrupoId(),
                    requestDTO.getMateriaId(),
                    requestDTO.getProfesorId(),
                    requestDTO.getPeriodoAcademicoId()
            )) {
                throw new ResourceAlreadyExistsException("Asignación de Materia", "combinación",
                        "Grupo ID: " + requestDTO.getGrupoId() +
                                ", Materia ID: " + requestDTO.getMateriaId() +
                                ", Profesor ID: " + requestDTO.getProfesorId() +
                                ", Periodo Académico ID: " + requestDTO.getPeriodoAcademicoId());
            }

            // Mapear los nuevos datos
            Grupo newGrupo = grupoRepository.findById(requestDTO.getGrupoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo", "ID", requestDTO.getGrupoId()));
            Materia newMateria = materiaRepository.findById(requestDTO.getMateriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Materia", "ID", requestDTO.getMateriaId()));
            Profesor newProfesor = profesorRepository.findById(requestDTO.getProfesorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Profesor", "ID", requestDTO.getProfesorId()));
            PeriodoAcademico newPeriodo = periodoAcademicoRepository.findById(requestDTO.getPeriodoAcademicoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Periodo Académico", "ID", requestDTO.getPeriodoAcademicoId()));

            existingAsignacion.setGrupo(newGrupo);
            existingAsignacion.setMateria(newMateria);
            existingAsignacion.setProfesor(newProfesor);
            existingAsignacion.setPeriodoAcademico(newPeriodo);

            AsignacionMateriaGrupo updatedAsignacion = asignacionRepository.save(existingAsignacion);
            return convertToDTO(updatedAsignacion);
        }).orElseThrow(() -> new ResourceNotFoundException("Asignación de Materia", "ID", id));
    }

    @Transactional
    public void deleteAsignacion(Long id) {
        if (!asignacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Asignación de Materia", "ID", id);
        }
        asignacionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> getAsignacionesByGrupoId(Long grupoId) {
        // Busca las asignaciones por el ID del grupo usando el repositorio
        return asignacionRepository.findByGrupoId(grupoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Dentro de AsignacionMateriaGrupoService.java
    @Transactional(readOnly = true)
    public List<AsignacionResponseDTO> getAsignacionesByMateriaId(Long materiaId) {
        return asignacionRepository.findByMateriaId(materiaId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}