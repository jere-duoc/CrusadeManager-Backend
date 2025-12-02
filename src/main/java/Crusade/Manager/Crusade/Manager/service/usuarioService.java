package Crusade.Manager.Crusade.Manager.service;

import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class usuarioService {

    @Autowired
    private usuarioRepository repo;

    @Autowired
    private JavaMailSender mailSender;

    // --- MÉTODOS BÁSICOS ---
    public List<usuarioModel> getAll() {
        return repo.findAll();
    }
    
    public Optional<usuarioModel> getById(Long id) {
        return repo.findById(id);
    }

    public Optional<usuarioModel> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public usuarioModel save(usuarioModel usuario) {
        return repo.save(usuario);
    }

    // --- LÓGICA DE NEGOCIO AVANZADA ---

    // 1. REGISTRO CON VALIDACIÓN Y CORREO (ROLLBACK)
    public usuarioModel registrarUsuario(usuarioModel usuario) throws Exception {
        
        // A. Validar duplicado
        if (repo.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // B. Configurar usuario (Código secreto y Bloqueo)
        String randomCode = UUID.randomUUID().toString();
        usuario.setVerificationCode(randomCode);
        usuario.setEnabled(false);

        usuarioModel saved = repo.save(usuario);

        // C. Intentar enviar correo (con Rollback si falla)
        try {
            String verificationLink = "http://localhost:8080/usuarios/verify?code=" + randomCode;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("crusademanager.auth@gmail.com");
            message.setTo(saved.getEmail());
            message.setSubject("Verifica tu cuenta - Crusade Manager");
            message.setText("¡Saludos, " + saved.getName() + "!\n\n" +
                    "Tu cuenta ha sido creada en los registros.\n" +
                    "Para activar tus credenciales, haz clic en el siguiente enlace:\n\n" +
                    verificationLink + "\n\n" +
                    "Si no fuiste tú, ignora este mensaje.\n" +
                    "El Emperador Protege.");

            mailSender.send(message);

        } catch (Exception e) {
            // SI FALLA EL CORREO: Borramos al usuario para no dejar "basura"
            System.out.println("ERROR CRÍTICO: Falló el envío de correo. Borrando usuario...");
            repo.deleteById(saved.getId());
            
            // Lanzamos error para que el Controller se entere
            throw new RuntimeException("Error al enviar el correo. No se pudo completar el registro.");
        }

        return saved;
    }

    // 2. VERIFICACIÓN DE CÓDIGO
    public boolean verificarCodigo(String code) {
        usuarioModel user = repo.findByVerificationCode(code);
        
        if (user != null) {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repo.save(user);
            return true;
        }
        return false;
    }

    // 3. VALIDACIÓN DE LOGIN (Lógica de negocio del Login)
    public String validarLogin(String passwordForm, usuarioModel usuarioEnBd) {
        // Chequeo 1: Contraseña
        if (!usuarioEnBd.getPassword().equals(passwordForm)) {
            return "Contraseña incorrecta";
        }
        // Chequeo 2: Verificación (Estado enabled)
        if (!usuarioEnBd.isEnabled()) {
            return "Debes verificar tu correo antes de entrar. Revisa tu bandeja de entrada.";
        }
        return "OK";
    }
}