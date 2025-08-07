package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionRequestDTO;
import com.verdugocode.administrador_escolar.dto.Asignacion.AsignacionResponseDTO;
import com.verdugocode.administrador_escolar.service.AsignacionMateriaGrupoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/asignaciones-materia")
public class AsignacionMateriaGrupoController {

    private final AsignacionMateriaGrupoService asignacionService;

    @Autowired
    public AsignacionMateriaGrupoController(AsignacionMateriaGrupoService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @GetMapping
    public ResponseEntity<List<AsignacionResponseDTO>> getAllAsignaciones() {
        return new ResponseEntity<>(asignacionService.getAllAsignaciones(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionResponseDTO> getAsignacionById(@PathVariable Long id) {
        return new ResponseEntity<>(asignacionService.getAsignacionById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AsignacionResponseDTO> createAsignacion(@Valid @RequestBody AsignacionRequestDTO requestDTO) {
        return new ResponseEntity<>(asignacionService.createAsignacion(requestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignacionResponseDTO> updateAsignacion(@PathVariable Long id, @Valid @RequestBody AsignacionRequestDTO requestDTO) {
        return new ResponseEntity<>(asignacionService.updateAsignacion(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsignacion(@PathVariable Long id) {
        asignacionService.deleteAsignacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}