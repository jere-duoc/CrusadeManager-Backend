package Crusade.Manager.Crusade.Manager.model;

import jakarta.persistence.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "usuarios")
@Schema(
    name = "Usuario",
    description = "Modelo que representa a un usuario dentro del sistema."
)
public class usuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
        description = "Identificador único del usuario",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Column(nullable = false)
    @Schema(
        description = "Nombre del usuario",
        example = "Juan Pérez",
        required = true
    )
    private String name;

    @Column(unique = true, nullable = false)
    @Schema(
        description = "Correo electrónico único del usuario",
        example = "juan@mail.com",
        required = true
    )
    private String email;

    @Column(columnDefinition = "TEXT")
    @Schema(
        description = "URL o base64 del avatar del usuario",
        example = "https://cdn.miapp.com/avatars/123.png"
    )
    private String avatar;

    @Column(nullable = false)
    @Schema(
        description = "Contraseña del usuario (encriptada en base de datos)",
        example = "12345678",
        required = true
    )
    private String password;

    // --- 1. NUEVOS CAMPOS PARA VERIFICACIÓN ---

    @Column(name = "verification_code", length = 64)
    @Schema(
        description = "Código de verificación enviado por correo al usuario",
        example = "A1B2C3D4E5F6",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String verificationCode;

    @Schema(
        description = "Indica si el usuario está habilitado para iniciar sesión",
        example = "false"
    )
    private boolean enabled;

    // ------------------------------------------

    public usuarioModel() {}

    public usuarioModel(String name, String email, String avatar, String password) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
    }

    // Getters y Setters normales
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getAvatar() { return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getVerificationCode() { return verificationCode; }

    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
