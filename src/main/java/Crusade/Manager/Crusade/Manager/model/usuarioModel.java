package Crusade.Manager.Crusade.Manager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class usuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String avatar; 

    @Column(nullable = false)
    private String password;

    // --- 1. NUEVOS CAMPOS PARA VERIFICACIÓN ---
    
    // Aquí guardaremos el código largo que va en el link
    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    // Esto dice si el usuario puede entrar (true) o no (false)
    private boolean enabled; 

    // ------------------------------------------

    public usuarioModel() {
    }

    public usuarioModel(String name, String email, String avatar, String password) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
    }

    // Getters y Setters normales
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- 2. GETTERS Y SETTERS NUEVOS ---

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}