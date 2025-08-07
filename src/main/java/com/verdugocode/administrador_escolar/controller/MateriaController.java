// Archivo: MateriaController.java
package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Materias.MateriaRequestDTO;
import com.verdugocode.administrador_escolar.dto.Materias.MateriaResponseDTO;
import com.verdugocode.administrador_escolar.service.MateriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/materias")
public class MateriaController {

    private final MateriaService materiaService;

    @Autowired
    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @GetMapping
    public ResponseEntity<List<MateriaResponseDTO>> getAllMaterias() {
        return new ResponseEntity<>(materiaService.getAllMaterias(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> getMateriaById(@PathVariable Long id) {
        return new ResponseEntity<>(materiaService.getMateriaById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MateriaResponseDTO> createMateria(@Valid @RequestBody MateriaRequestDTO requestDTO) {
        return new ResponseEntity<>(materiaService.createMateria(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaResponseDTO> updateMateria(@PathVariable Long id, @Valid @RequestBody MateriaRequestDTO requestDTO) {
        return new ResponseEntity<>(materiaService.updateMateria(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMateria(@PathVariable Long id) {
        materiaService.deleteMateria(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}