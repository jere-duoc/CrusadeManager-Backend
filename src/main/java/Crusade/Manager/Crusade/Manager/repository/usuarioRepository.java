package Crusade.Manager.Crusade.Manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import Crusade.Manager.Crusade.Manager.model.usuarioModel;

public interface usuarioRepository extends JpaRepository<usuarioModel, Long> {
  
    // Buscar por email (Para el Login y Registro)
    Optional<usuarioModel> findByEmail(String email);

    // --- NUEVO: Buscar por código de verificación ---
    // (Necesario para cuando hacen clic en el enlace del correo)
    usuarioModel findByVerificationCode(String code);

}