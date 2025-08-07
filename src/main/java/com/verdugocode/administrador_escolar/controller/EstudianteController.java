package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteRequestDTO; // Importar RequestDTO
import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteResponseDTO;
import com.verdugocode.administrador_escolar.service.EstudianteService;
import jakarta.validation.Valid; // Importar Valid para DTOs
import org.springframework.http.HttpStatus; // Importar HttpStatus para ResponseEntity.status
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
public class EstudianteController {
    private final EstudianteService estudianteService;

    public EstudianteController (EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping()
    public ResponseEntity<List<EstudianteResponseDTO>> getAllEstudiantes(){ // Nombre del método consistente
        List<EstudianteResponseDTO> estudiantes = estudianteService.findAllEstudiantes(); // Nombre del método consistente
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> getEstudianteById (@PathVariable Long id){
        // *** CORRECCIÓN EN EL TIPO DE RETORNO Y ASIGNACIÓN ***
        EstudianteResponseDTO estudiante = estudianteService.findById(id);
        return ResponseEntity.ok(estudiante);
    }

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> createEstudiante(@Valid @RequestBody EstudianteRequestDTO estudianteRequestDTO){
        EstudianteResponseDTO newEstudiante = estudianteService.createEstudiante(estudianteRequestDTO);
        return new ResponseEntity<>(newEstudiante, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> updateEstudiante(@PathVariable Long id, @Valid @RequestBody EstudianteRequestDTO estudianteRequestDTO){
        EstudianteResponseDTO updatedEstudiante = estudianteService.updateEstudiante(id, estudianteRequestDTO);
        return ResponseEntity.ok(updatedEstudiante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstudiante(@PathVariable Long id){
        estudianteService.deleteEstudiante(id);
        return ResponseEntity.noContent().build();
    }
}