package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Actividad.ActividadRequestDTO;
import com.verdugocode.administrador_escolar.dto.Actividad.ActividadResponseDTO;
import com.verdugocode.administrador_escolar.service.ActividadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    @Autowired
    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    @GetMapping
    public ResponseEntity<List<ActividadResponseDTO>> getAllActividades() {
        return new ResponseEntity<>(actividadService.getAllActividades(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> getActividadById(@PathVariable Long id) {
        return new ResponseEntity<>(actividadService.getActividadById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActividadResponseDTO> createActividad(@Valid @RequestBody ActividadRequestDTO requestDTO) {
        return new ResponseEntity<>(actividadService.createActividad(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActividadResponseDTO> updateActividad(@PathVariable Long id, @Valid @RequestBody ActividadRequestDTO requestDTO) {
        return new ResponseEntity<>(actividadService.updateActividad(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividad(@PathVariable Long id) {
        actividadService.deleteActividad(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/asignacion/{asignacionId}")
    public ResponseEntity<List<ActividadResponseDTO>> getActividadesByAsignacionId(@PathVariable Long asignacionId) {
        List<ActividadResponseDTO> actividades = actividadService.getActividadesByAsignacionId(asignacionId);
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }
}