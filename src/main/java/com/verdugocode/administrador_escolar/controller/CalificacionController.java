package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Calificacion.CalificacionRequestDTO;
import com.verdugocode.administrador_escolar.dto.Calificacion.CalificacionResponseDTO;
import com.verdugocode.administrador_escolar.service.CalificacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calificaciones")
public class CalificacionController {

    private final CalificacionService calificacionService;

    @Autowired
    public CalificacionController(CalificacionService calificacionService) {
        this.calificacionService = calificacionService;
    }

    @GetMapping
    public ResponseEntity<List<CalificacionResponseDTO>> getAllCalificaciones() {
        return new ResponseEntity<>(calificacionService.getAllCalificaciones(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> getCalificacionById(@PathVariable Long id) {
        return new ResponseEntity<>(calificacionService.getCalificacionById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CalificacionResponseDTO> createCalificacion(@Valid @RequestBody CalificacionRequestDTO requestDTO) {
        return new ResponseEntity<>(calificacionService.createCalificacion(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalificacionResponseDTO> updateCalificacion(@PathVariable Long id, @Valid @RequestBody CalificacionRequestDTO requestDTO) {
        return new ResponseEntity<>(calificacionService.updateCalificacion(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalificacion(@PathVariable Long id) {
        calificacionService.deleteCalificacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}