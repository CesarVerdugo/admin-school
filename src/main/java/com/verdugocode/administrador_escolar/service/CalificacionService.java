package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.Calificacion.CalificacionRequestDTO;
import com.verdugocode.administrador_escolar.dto.Calificacion.CalificacionResponseDTO;
import com.verdugocode.administrador_escolar.entity.Actividad;
import com.verdugocode.administrador_escolar.entity.Calificacion;
import com.verdugocode.administrador_escolar.entity.Estudiante;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.ActividadRepository;
import com.verdugocode.administrador_escolar.repository.CalificacionRepository;
import com.verdugocode.administrador_escolar.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final EstudianteRepository estudianteRepository;
    private final ActividadRepository actividadRepository;
    private final EstudianteService estudianteService;
    private final ActividadService actividadService;

    @Autowired
    public CalificacionService(CalificacionRepository calificacionRepository,
                               EstudianteRepository estudianteRepository,
                               ActividadRepository actividadRepository,
                               EstudianteService estudianteService,
                               ActividadService actividadService) {
        this.calificacionRepository = calificacionRepository;
        this.estudianteRepository = estudianteRepository;
        this.actividadRepository = actividadRepository;
        this.estudianteService = estudianteService;
        this.actividadService = actividadService;
    }

    // --- Métodos de Mapeo (Conversores) ---
    private CalificacionResponseDTO convertToDTO(Calificacion calificacion) {
        CalificacionResponseDTO dto = new CalificacionResponseDTO();
        dto.setId(calificacion.getId());
        dto.setValor(calificacion.getValor());
        dto.setComentarios(calificacion.getComentarios());
        dto.setFechaCalificacion(calificacion.getFechaCalificacion());
        dto.setEstudiante(estudianteService.getEstudianteById(calificacion.getEstudiante().getId()));
        dto.setActividad(actividadService.getActividadById(calificacion.getActividad().getId()));
        return dto;
    }

    private Calificacion convertToEntity(CalificacionRequestDTO requestDTO) {
        Estudiante estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "ID", requestDTO.getEstudianteId()));
        Actividad actividad = actividadRepository.findById(requestDTO.getActividadId())
                .orElseThrow(() -> new ResourceNotFoundException("Actividad", "ID", requestDTO.getActividadId()));

        Calificacion calificacion = new Calificacion();
        calificacion.setEstudiante(estudiante);
        calificacion.setActividad(actividad);
        calificacion.setValor(requestDTO.getValor());
        calificacion.setComentarios(requestDTO.getComentarios());
        calificacion.setFechaCalificacion(LocalDate.now());
        return calificacion;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---
    @Transactional(readOnly = true)
    public List<CalificacionResponseDTO> getAllCalificaciones() {
        return calificacionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CalificacionResponseDTO getCalificacionById(Long id) {
        return calificacionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación", "ID", id));
    }

    @Transactional
    public CalificacionResponseDTO createCalificacion(CalificacionRequestDTO requestDTO) {
        if (calificacionRepository.existsByEstudianteIdAndActividadId(requestDTO.getEstudianteId(), requestDTO.getActividadId())) {
            throw new ResourceAlreadyExistsException("Calificación", "combinación", "Estudiante ID: " + requestDTO.getEstudianteId() + ", Actividad ID: " + requestDTO.getActividadId());
        }
        Calificacion calificacion = convertToEntity(requestDTO);
        Calificacion savedCalificacion = calificacionRepository.save(calificacion);
        return convertToDTO(savedCalificacion);
    }

    @Transactional
    public CalificacionResponseDTO updateCalificacion(Long id, CalificacionRequestDTO requestDTO) {
        return calificacionRepository.findById(id).map(existingCalificacion -> {
            Estudiante estudiante = estudianteRepository.findById(requestDTO.getEstudianteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estudiante", "ID", requestDTO.getEstudianteId()));
            Actividad actividad = actividadRepository.findById(requestDTO.getActividadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad", "ID", requestDTO.getActividadId()));

            existingCalificacion.setEstudiante(estudiante);
            existingCalificacion.setActividad(actividad);
            existingCalificacion.setValor(requestDTO.getValor());
            existingCalificacion.setComentarios(requestDTO.getComentarios());

            Calificacion updatedCalificacion = calificacionRepository.save(existingCalificacion);
            return convertToDTO(updatedCalificacion);
        }).orElseThrow(() -> new ResourceNotFoundException("Calificación", "ID", id));
    }

    @Transactional
    public void deleteCalificacion(Long id) {
        if (!calificacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Calificación", "ID", id);
        }
        calificacionRepository.deleteById(id);
    }
}