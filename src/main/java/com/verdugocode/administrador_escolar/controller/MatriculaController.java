package com.verdugocode.administrador_escolar.controller;

import com.verdugocode.administrador_escolar.dto.Matriculas.MatriculaRequestDTO;
import com.verdugocode.administrador_escolar.dto.Matriculas.MatriculaResponseDTO;
import com.verdugocode.administrador_escolar.service.MatriculaService;
import jakarta.validation.Valid; // Para habilitar las validaciones de los DTOs
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marca esta clase como un controlador REST.
@RequestMapping("/api/v1/matriculas") // Define la ruta base para todas las operaciones de este controlador.
public class MatriculaController {

    private final MatriculaService matriculaService;

    @Autowired // Inyección de dependencias del servicio.
    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    /**
     * Obtiene todas las matrículas registradas.
     * GET /api/matriculas
     * @return Una lista de MatriculaResponseDTO.
     */
    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> getAllMatriculas() {
        List<MatriculaResponseDTO> matriculas = matriculaService.getAllMatriculas();
        return ResponseEntity.ok(matriculas); // Retorna 200 OK con la lista de matrículas.
    }

    /**
     * Obtiene una matrícula por su ID.
     * GET /api/matriculas/{id}
     * @param id El ID de la matrícula a buscar.
     * @return El MatriculaResponseDTO correspondiente.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> getMatriculaById(@PathVariable Long id) {
        MatriculaResponseDTO matricula = matriculaService.getMatriculaById(id);
        return ResponseEntity.ok(matricula); // Retorna 200 OK con la matrícula encontrada.
    }

    /**
     * Crea una nueva matrícula.
     * POST /api/matriculas
     * @param matriculaRequestDTO Los datos de la matrícula a crear.
     * @return El MatriculaResponseDTO de la matrícula creada.
     */
    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> createMatricula(@Valid @RequestBody MatriculaRequestDTO matriculaRequestDTO) {
        MatriculaResponseDTO newMatricula = matriculaService.createMatricula(matriculaRequestDTO);
        return new ResponseEntity<>(newMatricula, HttpStatus.CREATED); // Retorna 201 CREATED con la matrícula creada.
    }

    /**
     * Actualiza una matrícula existente por su ID.
     * PUT /api/matriculas/{id}
     * @param id El ID de la matrícula a actualizar.
     * @param matriculaRequestDTO Los nuevos datos de la matrícula.
     * @return El MatriculaResponseDTO de la matrícula actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> updateMatricula(@PathVariable Long id, @Valid @RequestBody MatriculaRequestDTO matriculaRequestDTO) {
        MatriculaResponseDTO updatedMatricula = matriculaService.updateMatricula(id, matriculaRequestDTO);
        return ResponseEntity.ok(updatedMatricula); // Retorna 200 OK con la matrícula actualizada.
    }

    /**
     * Elimina una matrícula por su ID.
     * DELETE /api/matriculas/{id}
     * @param id El ID de la matrícula a eliminar.
     * @return Una respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatricula(@PathVariable Long id) {
        matriculaService.deleteMatricula(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content.
    }
}
