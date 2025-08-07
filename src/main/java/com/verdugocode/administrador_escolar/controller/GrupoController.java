// Archivo: GrupoController.java
package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Grupos.GrupoRequestDTO;
import com.verdugocode.administrador_escolar.dto.Grupos.GrupoResponseDTO;
import com.verdugocode.administrador_escolar.dto.Estudiantes.EstudianteResponseDTO;
import com.verdugocode.administrador_escolar.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grupos")
public class GrupoController {

    private final GrupoService grupoService;

    @Autowired
    public GrupoController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    // --- Endpoints de CRUD existentes ---
    @GetMapping
    public ResponseEntity<List<GrupoResponseDTO>> getAllGrupos() {
        return new ResponseEntity<>(grupoService.getAllGrupos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoResponseDTO> getGrupoById(@PathVariable Long id) {
        return new ResponseEntity<>(grupoService.getGrupoById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GrupoResponseDTO> createGrupo(@Valid @RequestBody GrupoRequestDTO requestDTO) {
        return new ResponseEntity<>(grupoService.createGrupo(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrupoResponseDTO> updateGrupo(@PathVariable Long id, @Valid @RequestBody GrupoRequestDTO requestDTO) {
        return new ResponseEntity<>(grupoService.updateGrupo(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrupo(@PathVariable Long id) {
        grupoService.deleteGrupo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // --- Endpoint para obtener estudiantes del grupo ---
    @GetMapping("/{grupoId}/estudiantes")
    public ResponseEntity<List<EstudianteResponseDTO>> getEstudiantesByGrupoId(@PathVariable Long grupoId) {
        List<EstudianteResponseDTO> estudiantes = grupoService.getEstudiantesByGrupoId(grupoId);
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }
}