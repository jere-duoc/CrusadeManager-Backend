package Crusade.Manager.Crusade.Manager.controller;

import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.service.usuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ReflectionUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
@Validated
public class usuarioController {

    @Autowired
    private usuarioService service;

    // --- 1. REGISTRO (POST) ---
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody usuarioModel usuario) {
        try {
            // Llama a service.registrarUsuario
            usuarioModel saved = service.registrarUsuario(usuario);
            return ResponseEntity.created(URI.create("/usuarios/" + saved.getId())).body(saved);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error desconocido del servidor.");
        }
    }

    // --- 2. VERIFICACIÓN (GET) ---
    @GetMapping("/verify")
    public void verifyUser(@Param("code") String code, HttpServletResponse response) throws IOException {
        // Llama a service.verificarCodigo
        boolean exito = service.verificarCodigo(code); 
        
        if (exito) {
            response.sendRedirect("http://localhost:5173/login?status=success");
        } else {
            response.sendRedirect("http://localhost:5173/login?status=error");
        }
    }

    // ...
// --- 3. LOGIN (POST) ---
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody usuarioModel datos) {
    // 1. Buscamos el usuario por email
    return service.findByEmail(datos.getEmail())
        .map(usuario -> {
            // 2. Aquí llamamos al servicio para la lógica de validación
            String resultado = service.validarLogin(datos.getPassword(), usuario);
            
            if (resultado.equals("OK")) {
                // Si el servicio devuelve OK (contraseña correcta y habilitado)
                return ResponseEntity.ok(usuario); // ÉXITO
            } else if (resultado.contains("Contraseña")) {
                // Contraseña incorrecta -> 401
                return ResponseEntity.status(401).body(resultado); 
            } else {
                // Cuenta no verificada -> 403
                return ResponseEntity.status(403).body(resultado);
            }
        })
        .orElse(ResponseEntity.status(404).body("Usuario no existe")); // Si no encuentra el email
}
// ...

    // --- 4. MÉTODOS ESTÁNDAR ---
    
    @GetMapping
    public ResponseEntity<List<usuarioModel>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<usuarioModel> getById(@PathVariable Long id) {
        return service.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<usuarioModel> update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
            usuarioModel user = service.getById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(usuarioModel.class, key);
                if (field != null) { field.setAccessible(true); ReflectionUtils.setField(field, user, value); }
            });
        return ResponseEntity.ok(service.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.getById(id).isPresent()) { service.delete(id); return ResponseEntity.noContent().build(); }
        else { return ResponseEntity.notFound().build(); }
    }
}