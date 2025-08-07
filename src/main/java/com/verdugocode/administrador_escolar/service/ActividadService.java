// Archivo: ActividadService.java
package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Actividad.ActividadRequestDTO;
import com.verdugocode.administrador_escolar.dto.Actividad.ActividadResponseDTO;
import com.verdugocode.administrador_escolar.entity.Actividad;
import com.verdugocode.administrador_escolar.entity.AsignacionMateriaGrupo;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.ActividadRepository;
import com.verdugocode.administrador_escolar.repository.AsignacionMateriaGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final AsignacionMateriaGrupoRepository asignacionRepository;

    @Autowired
    public ActividadService(ActividadRepository actividadRepository, AsignacionMateriaGrupoRepository asignacionRepository) {
        this.actividadRepository = actividadRepository;
        this.asignacionRepository = asignacionRepository;
    }

    // --- Métodos de Mapeo (Conversores) ---
    private ActividadResponseDTO convertToDTO(Actividad actividad) {
        ActividadResponseDTO dto = new ActividadResponseDTO();
        dto.setId(actividad.getId());
        dto.setTitulo(actividad.getTitulo());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setFechaEntrega(actividad.getFechaEntrega());
        // Llenamos solo el ID de la asignación
        dto.setAsignacionMateriaGrupoId(actividad.getAsignacionMateriaGrupo().getId());
        return dto;
    }

    private Actividad convertToEntity(ActividadRequestDTO requestDTO) {
        AsignacionMateriaGrupo asignacion = asignacionRepository.findById(requestDTO.getAsignacionMateriaGrupoId())
                .orElseThrow(() -> new ResourceNotFoundException("AsignacionMateriaGrupo", "ID", requestDTO.getAsignacionMateriaGrupoId()));

        Actividad actividad = new Actividad();
        actividad.setTitulo(requestDTO.getTitulo());
        actividad.setDescripcion(requestDTO.getDescripcion());
        actividad.setFechaEntrega(requestDTO.getFechaEntrega());
        actividad.setAsignacionMateriaGrupo(asignacion);
        return actividad;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---
    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> getAllActividades() {
        return actividadRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ActividadResponseDTO getActividadById(Long id) {
        return actividadRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad", "ID", id));
    }

    @Transactional
    public ActividadResponseDTO createActividad(ActividadRequestDTO requestDTO) {
        Actividad actividad = convertToEntity(requestDTO);
        Actividad savedActividad = actividadRepository.save(actividad);
        return convertToDTO(savedActividad);
    }

    @Transactional
    public ActividadResponseDTO updateActividad(Long id, ActividadRequestDTO requestDTO) {
        return actividadRepository.findById(id).map(existingActividad -> {
            AsignacionMateriaGrupo asignacion = asignacionRepository.findById(requestDTO.getAsignacionMateriaGrupoId())
                    .orElseThrow(() -> new ResourceNotFoundException("AsignacionMateriaGrupo", "ID", requestDTO.getAsignacionMateriaGrupoId()));

            existingActividad.setTitulo(requestDTO.getTitulo());
            existingActividad.setDescripcion(requestDTO.getDescripcion());
            existingActividad.setFechaEntrega(requestDTO.getFechaEntrega());
            existingActividad.setAsignacionMateriaGrupo(asignacion);

            Actividad updatedActividad = actividadRepository.save(existingActividad);
            return convertToDTO(updatedActividad);
        }).orElseThrow(() -> new ResourceNotFoundException("Actividad", "ID", id));
    }

    @Transactional
    public void deleteActividad(Long id) {
        if (!actividadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Actividad", "ID", id);
        }
        actividadRepository.deleteById(id);
    }

    // --- Nuevo método para obtener actividades por asignación de materia-grupo ---
    @Transactional(readOnly = true)
    public List<ActividadResponseDTO> getActividadesByAsignacionId(Long asignacionMateriaGrupoId) {
        return actividadRepository.findByAsignacionMateriaGrupoId(asignacionMateriaGrupoId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}