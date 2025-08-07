package com.verdugocode.administrador_escolar.service;

import com.verdugocode.administrador_escolar.dto.PeriodoAcademico.PeriodoAcademicoRequestDTO;
import com.verdugocode.administrador_escolar.dto.PeriodoAcademico.PeriodoAcademicoResponseDTO;
import com.verdugocode.administrador_escolar.entity.PeriodoAcademico;
import com.verdugocode.administrador_escolar.exception.ResourceAlreadyExistsException;
import com.verdugocode.administrador_escolar.exception.ResourceNotFoundException;
import com.verdugocode.administrador_escolar.repository.PeriodoAcademicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeriodoAcademicoService {

    private final PeriodoAcademicoRepository periodoAcademicoRepository;

    @Autowired
    public PeriodoAcademicoService(PeriodoAcademicoRepository periodoAcademicoRepository) {
        this.periodoAcademicoRepository = periodoAcademicoRepository;
    }

    // --- Métodos de Mapeo (Conversores) ---
    private PeriodoAcademicoResponseDTO convertToDTO(PeriodoAcademico periodo) {
        PeriodoAcademicoResponseDTO dto = new PeriodoAcademicoResponseDTO();
        dto.setId(periodo.getId());
        dto.setNombre(periodo.getNombre());
        dto.setFechaInicio(periodo.getFechaInicio());
        dto.setFechaFin(periodo.getFechaFin());
        return dto;
    }

    private PeriodoAcademico convertToEntity(PeriodoAcademicoRequestDTO requestDTO) {
        PeriodoAcademico periodo = new PeriodoAcademico();
        periodo.setNombre(requestDTO.getNombre());
        periodo.setFechaInicio(requestDTO.getFechaInicio());
        periodo.setFechaFin(requestDTO.getFechaFin());
        return periodo;
    }

    // --- Métodos de Lógica de Negocio (CRUD) ---

    @Transactional(readOnly = true)
    public List<PeriodoAcademicoResponseDTO> getAllPeriodos() {
        return periodoAcademicoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PeriodoAcademicoResponseDTO getPeriodoById(Long id) {
        return periodoAcademicoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo Académico", "ID", id));
    }

    @Transactional
    public PeriodoAcademicoResponseDTO createPeriodo(PeriodoAcademicoRequestDTO requestDTO) {
        // Validar unicidad del nombre
        if (periodoAcademicoRepository.existsByNombre(requestDTO.getNombre())) {
            throw new ResourceAlreadyExistsException("Periodo Académico", "nombre", requestDTO.getNombre());
        }

        PeriodoAcademico periodo = convertToEntity(requestDTO);
        PeriodoAcademico savedPeriodo = periodoAcademicoRepository.save(periodo);
        return convertToDTO(savedPeriodo);
    }

    @Transactional
    public PeriodoAcademicoResponseDTO updatePeriodo(Long id, PeriodoAcademicoRequestDTO requestDTO) {
        return periodoAcademicoRepository.findById(id).map(existingPeriodo -> {
            // Validar unicidad del nombre si cambia
            if (!existingPeriodo.getNombre().equals(requestDTO.getNombre()) &&
                    periodoAcademicoRepository.existsByNombre(requestDTO.getNombre())) {
                throw new ResourceAlreadyExistsException("Periodo Académico", "nombre", requestDTO.getNombre());
            }

            existingPeriodo.setNombre(requestDTO.getNombre());
            existingPeriodo.setFechaInicio(requestDTO.getFechaInicio());
            existingPeriodo.setFechaFin(requestDTO.getFechaFin());

            PeriodoAcademico updatedPeriodo = periodoAcademicoRepository.save(existingPeriodo);
            return convertToDTO(updatedPeriodo);
        }).orElseThrow(() -> new ResourceNotFoundException("Periodo Académico", "ID", id));
    }

    @Transactional
    public void deletePeriodo(Long id) {
        if (!periodoAcademicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Periodo Académico", "ID", id);
        }
        // Nota: Considera las reglas de eliminación en cascada en tu base de datos si otros
        // objetos (como AsignacionMateriaGrupo) tienen una referencia a este periodo.
        periodoAcademicoRepository.deleteById(id);
    }
}