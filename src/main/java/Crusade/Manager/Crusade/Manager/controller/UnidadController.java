package main.java.Crusade.Manager.Crusade.Manager.controller;

import main.java.Crusade.Manager.Crusade.Manager.model.Unidad;
import main.java.Crusade.Manager.Crusade.Manager.service.UnidadService;
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

    @GetMapping
    public List<Unidad> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Unidad getById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/roster/{rosterId}")
    public List<Unidad> getByRoster(@PathVariable String rosterId) {
        return service.findByRosterId(rosterId);
    }

    @PostMapping
    public Unidad create(@RequestBody Unidad unidad) {
        return service.save(unidad);
    }

    @PutMapping("/{id}")
    public Unidad update(@PathVariable String id, @RequestBody Unidad unidad) {
        unidad.setId(id);
        return service.save(unidad);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
