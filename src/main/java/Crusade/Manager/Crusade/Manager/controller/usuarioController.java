package Crusade.Manager.Crusade.Manager.controller;

import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.service.usuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.repository.query.Param;
import org.springframework.util.ReflectionUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Crea un usuario nuevo, valida correo, guarda en la BD y envía código de verificación."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> save(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del usuario a registrar",
            required = true,
            content = @Content(
                schema = @Schema(implementation = usuarioModel.class),
                examples = {
                    @ExampleObject(name = "Ejemplo registro", value = "{ \"email\": \"test@mail.com\", \"password\": \"12345\", \"nombre\": \"Juan\" }")
                }
            )
        )
        @Valid @RequestBody usuarioModel usuario
    ) {
        try {
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
    @Operation(
        summary = "Verificar correo del usuario",
        description = "Verifica el código enviado al email. Redirige al login con estado 'success' o 'error'."
    )
    @Parameter(
        name = "code",
        description = "Código de verificación enviado al email del usuario",
        required = true,
        example = "A1B2C3D4"
    )
    @GetMapping("/verify")
    public void verifyUser(@Param("code") String code, HttpServletResponse response) throws IOException {
        boolean exito = service.verificarCodigo(code);

        if (exito) {
            response.sendRedirect("http://warhammer-app-final.s3-website-us-east-1.amazonaws.com/login?status=success");
        } else {
            response.sendRedirect("http://warhammer-app-final.s3-website-us-east-1.amazonaws.com/login?status=error");
        }
    }

    // --- 3. LOGIN (POST) ---
    @Operation(
        summary = "Login de usuario",
        description = "Valida email, contraseña y verificación de cuenta. Devuelve el usuario si el login es exitoso."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Contraseña incorrecta"),
        @ApiResponse(responseCode = "403", description = "Usuario no verificado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Email y contraseña del usuario",
            required = true,
            content = @Content(
                schema = @Schema(implementation = usuarioModel.class),
                examples = {
                    @ExampleObject(
                        name = "Ejemplo login",
                        value = "{ \"email\": \"test@mail.com\", \"password\": \"12345\" }"
                    )
                }
            )
        )
        @RequestBody usuarioModel datos
    ) {
        return service.findByEmail(datos.getEmail())
            .map(usuario -> {

                String resultado = service.validarLogin(datos.getPassword(), usuario);

                if (resultado.equals("OK")) {
                    return ResponseEntity.ok(usuario);

                } else if (resultado.contains("Contraseña")) {
                    return ResponseEntity.status(401).body(resultado);

                } else {
                    return ResponseEntity.status(403).body(resultado);
                }
            })
            .orElse(ResponseEntity.status(404).body("Usuario no existe"));
    }

    // --- 4. OBTENER TODOS ---
    @Operation(
        summary = "Obtener todos los usuarios",
        description = "Devuelve una lista completa de todos los usuarios registrados."
    )
    @GetMapping
    public ResponseEntity<List<usuarioModel>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // --- 5. OBTENER POR ID ---
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Busca un usuario específico por su identificador."
    )
    @Parameter(name = "id", example = "1")
    @GetMapping("/{id}")
    public ResponseEntity<usuarioModel> getById(@PathVariable Long id) {
        return service.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // --- 6. PATCH (ACTUALIZACIÓN PARCIAL) ---
    @Operation(
        summary = "Actualizar parcialmente un usuario",
        description = "Permite modificar campos específicos del usuario utilizando una solicitud PATCH."
    )
    @Parameter(
        name = "id",
        description = "ID del usuario a modificar",
        required = true,
        example = "1"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<usuarioModel> update(
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Campos a actualizar en formato JSON",
            content = @Content(
                examples = {
                    @ExampleObject(
                        name = "Ejemplo PATCH",
                        value = "{ \"nombre\": \"NuevoNombre\", \"password\": \"NuevaPass\" }"
                    )
                }
            )
        )
        @RequestBody Map<String, Object> updates
    ) {
        usuarioModel user = service.getById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(usuarioModel.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            }
        });

        return ResponseEntity.ok(service.save(user));
    }

    // --- 7. ELIMINAR ---
    @Operation(
        summary = "Eliminar un usuario",
        description = "Elimina un usuario específico de la base de datos basado en su ID."
    )
    @Parameter(name = "id", example = "1")
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
