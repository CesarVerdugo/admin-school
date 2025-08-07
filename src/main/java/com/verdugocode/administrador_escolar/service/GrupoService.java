// Archivo: GrupoService.java
package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionResponseDTO;
import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteResponseDTO;
import com.verdugocode.administrador_escolar.dto.Grupos.GrupoRequestDTO;
import com.verdugocode.administrador_escolar.dto.Grupos.GrupoResponseDTO;
import com.verdugocode.administrador_escolar.entity.Grupo;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final AsignacionMateriaGrupoService asignacionService;
    private final MatriculaService matriculaService; // Nueva dependencia

    @Autowired
    public GrupoService(GrupoRepository grupoRepository, AsignacionMateriaGrupoService asignacionService, MatriculaService matriculaService) {
        this.grupoRepository = grupoRepository;
        this.asignacionService = asignacionService;
        this.matriculaService = matriculaService; // Inyección de la dependencia
    }

    // --- Métodos de Mapeo (Conversores) ---
    private GrupoResponseDTO convertToDTO(Grupo grupo) {
        GrupoResponseDTO dto = new GrupoResponseDTO();
        dto.setId(grupo.getId());
        dto.setNombreGrupo(grupo.getNombreGrupo());
        dto.setGrado(grupo.getGrado());
        dto.setJornada(grupo.getJornada());
        // Llenamos las asignaciones de materia usando el servicio correspondiente
        dto.setAsignacionesMateria(
                asignacionService.getAsignacionesByGrupoId(grupo.getId()).stream()
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    private Grupo convertToEntity(GrupoRequestDTO requestDTO) {
        Grupo grupo = new Grupo();
        grupo.setNombreGrupo(requestDTO.getNombreGrupo());
        grupo.setGrado(requestDTO.getGrado());
        grupo.setJornada(requestDTO.getJornada());
        return grupo;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---
    @Transactional(readOnly = true)
    public List<GrupoResponseDTO> getAllGrupos() {
        return grupoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GrupoResponseDTO getGrupoById(Long id) {
        return grupoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Grupo", "ID", id));
    }

    @Transactional
    public GrupoResponseDTO createGrupo(GrupoRequestDTO requestDTO) {
        if (grupoRepository.existsByNombreGrupo(requestDTO.getNombreGrupo())) {
            throw new ResourceAlreadyExistsException("Grupo", "nombreGrupo", requestDTO.getNombreGrupo());
        }
        Grupo grupo = convertToEntity(requestDTO);
        Grupo savedGrupo = grupoRepository.save(grupo);
        return convertToDTO(savedGrupo);
    }

    @Transactional
    public GrupoResponseDTO updateGrupo(Long id, GrupoRequestDTO requestDTO) {
        return grupoRepository.findById(id).map(existingGrupo -> {
            if (!existingGrupo.getNombreGrupo().equals(requestDTO.getNombreGrupo()) &&
                    grupoRepository.existsByNombreGrupo(requestDTO.getNombreGrupo())) {
                throw new ResourceAlreadyExistsException("Grupo", "nombreGrupo", requestDTO.getNombreGrupo());
            }

            existingGrupo.setNombreGrupo(requestDTO.getNombreGrupo());
            existingGrupo.setGrado(requestDTO.getGrado());
            existingGrupo.setJornada(requestDTO.getJornada());

            Grupo updatedGrupo = grupoRepository.save(existingGrupo);
            return convertToDTO(updatedGrupo);
        }).orElseThrow(() -> new ResourceNotFoundException("Grupo", "ID", id));
    }

    @Transactional
    public void deleteGrupo(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grupo", "ID", id);
        }
        grupoRepository.deleteById(id);
    }

    // --- Nuevo método para obtener estudiantes (delegado a MatriculaService) ---
    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> getEstudiantesByGrupoId(Long grupoId) {
        // Llama al servicio de matrícula para obtener los estudiantes
        return matriculaService.getEstudiantesByGrupoId(grupoId);
    }
}