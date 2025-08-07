package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.PeriodoAcademico.PeriodoAcademicoRequestDTO;
import com.verdugocode.administrador_escolar.dto.PeriodoAcademico.PeriodoAcademicoResponseDTO;
import com.verdugocode.administrador_escolar.service.PeriodoAcademicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/periodos-academicos")
public class PeriodoAcademicoController {

    private final PeriodoAcademicoService periodoAcademicoService;

    @Autowired
    public PeriodoAcademicoController(PeriodoAcademicoService periodoAcademicoService) {
        this.periodoAcademicoService = periodoAcademicoService;
    }

    @GetMapping
    public ResponseEntity<List<PeriodoAcademicoResponseDTO>> getAllPeriodos() {
        return new ResponseEntity<>(periodoAcademicoService.getAllPeriodos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoAcademicoResponseDTO> getPeriodoById(@PathVariable Long id) {
        return new ResponseEntity<>(periodoAcademicoService.getPeriodoById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PeriodoAcademicoResponseDTO> createPeriodo(@Valid @RequestBody PeriodoAcademicoRequestDTO requestDTO) {
        return new ResponseEntity<>(periodoAcademicoService.createPeriodo(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodoAcademicoResponseDTO> updatePeriodo(@PathVariable Long id, @Valid @RequestBody PeriodoAcademicoRequestDTO requestDTO) {
        return new ResponseEntity<>(periodoAcademicoService.updatePeriodo(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeriodo(@PathVariable Long id) {
        periodoAcademicoService.deletePeriodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}