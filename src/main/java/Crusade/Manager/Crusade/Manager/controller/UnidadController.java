package Crusade.Manager.Crusade.Manager.controller;

import Crusade.Manager.Crusade.Manager.model.Unidad;
import Crusade.Manager.Crusade.Manager.service.UnidadService;
import Crusade.Manager.Crusade.Manager.exception.ResourceNotFoundException;
import Crusade.Manager.Crusade.Manager.exception.BadRequestException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/unidades")
@CrossOrigin(origins = "*")
public class UnidadController {

    private final UnidadService service;

    public UnidadController(UnidadService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todas las unidades",
               description = "Devuelve una lista completa de unidades registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Unidad>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener una unidad por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidad encontrada"),
            @ApiResponse(responseCode = "404", description = "Unidad no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Unidad> getById(
            @Parameter(description = "ID de la unidad", required = true)
            @PathVariable String id) {

        Unidad unidad = service.findById(id);
        if (unidad == null) {
            throw new ResourceNotFoundException("Unidad con id " + id + " no encontrada");
        }
        return ResponseEntity.ok(unidad);
    }

    @Operation(summary = "Obtener unidades por rosterId")
    @ApiResponse(responseCode = "200", description = "Unidades obtenidas correctamente")
    @GetMapping("/roster/{rosterId}")
    public ResponseEntity<List<Unidad>> getByRoster(
            @Parameter(description = "ID del roster", required = true)
            @PathVariable String rosterId) {

        return ResponseEntity.ok(service.findByRosterId(rosterId));
    }

    @Operation(summary = "Crear una unidad nueva")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidad creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Unidad> create(
            @RequestBody Unidad unidad) {

        if (unidad.getNombre() == null || unidad.getNombre().isBlank()) {
            throw new BadRequestException("El nombre de la unidad es obligatorio");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(unidad));
    }

    @Operation(summary = "Actualizar unidad existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidad actualizada"),
            @ApiResponse(responseCode = "404", description = "Unidad no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Unidad> update(
            @PathVariable String id,
            @RequestBody Unidad unidad) {

        Unidad existing = service.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("No se puede actualizar. Unidad con id " + id + " no encontrada");
        }

        unidad.setId(id);
        return ResponseEntity.ok(service.save(unidad));
    }

    @Operation(summary = "Eliminar una unidad por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Unidad eliminada"),
            @ApiResponse(responseCode = "404", description = "Unidad no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Unidad existing = service.findById(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No se puede eliminar. Unidad con id " + id + " no encontrada");
        }

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
