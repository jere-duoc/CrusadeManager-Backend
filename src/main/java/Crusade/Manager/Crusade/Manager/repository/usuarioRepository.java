package Crusade.Manager.Crusade.Manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Crusade.Manager.Crusade.Manager.model.usuarioModel;

public interface usuarioRepository extends JpaRepository<usuarioModel, Long> {
    usuarioModel findByEmail(String email);
}
