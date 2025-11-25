package Crusade.Manager.Crusade.Manager.controller;

import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.service.usuarioService;

import org.springframework.util.ReflectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
@Validated
public class usuarioController {

    private final usuarioService service;

    public usuarioController(usuarioService service) {
        this.service = service;
    }

    //Devuelve todos los usuarios
    @GetMapping
    public ResponseEntity<List<usuarioModel>> getAll() {
        List<usuarioModel> users = service.getAll();
        return ResponseEntity.ok(users);
    }

    //Devuelve un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<usuarioModel> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Crea un nuevo usuario
    @PostMapping
    public ResponseEntity<usuarioModel> save(@Valid @RequestBody usuarioModel usuario) {
        usuarioModel saved = service.save(usuario);
        return ResponseEntity.created(URI.create("/usuarios/" + saved.getId())).body(saved);
    }

    //Valida el login de un usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody usuarioModel datos) {

    return service.findByEmail(datos.getEmail())
            .map(usuario -> {
                if (usuario.getPassword().equals(datos.getPassword())) {
                    return ResponseEntity.ok(usuario);
                } else {
                    return ResponseEntity.status(401).body("Contraseña incorrecta");
                }
            })
            .orElse(ResponseEntity.status(404).body("Usuario no existe"));
    }

    //Actualiza u campo de un usuario por su ID
    @PatchMapping("/{id}")
    public ResponseEntity<usuarioModel> update(
        @PathVariable Long id, 
        @RequestBody Map<String, Object> updates) {

            usuarioModel user = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(usuarioModel.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user, value);
                }
        });

        usuarioModel updatedUser = service.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    //Elimina un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();   
        }
    }
}