package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Profesor.ProfesorRequestDTO;
import com.verdugocode.administrador_escolar.dto.Profesor.ProfesorResponseDTO;
import com.verdugocode.administrador_escolar.service.ProfesorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profesores")
public class ProfesorController {

    private final ProfesorService profesorService;

    @Autowired
    public ProfesorController(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @GetMapping
    public ResponseEntity<List<ProfesorResponseDTO>> getAllProfesores() {
        return new ResponseEntity<>(profesorService.getAllProfesores(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> getProfesorById(@PathVariable Long id) {
        return new ResponseEntity<>(profesorService.getProfesorById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProfesorResponseDTO> createProfesor(@Valid @RequestBody ProfesorRequestDTO requestDTO) {
        return new ResponseEntity<>(profesorService.createProfesor(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorResponseDTO> updateProfesor(@PathVariable Long id, @Valid @RequestBody ProfesorRequestDTO requestDTO) {
        return new ResponseEntity<>(profesorService.updateProfesor(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfesor(@PathVariable Long id) {
        profesorService.deleteProfesor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}