package Crusade.Manager.Crusade.Manager.service;

import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class usuarioService {

    @Autowired
    private usuarioRepository repo;

    public List<usuarioModel> getAll() {
        return repo.findAll();
    }
    
    public Optional<usuarioModel> getById(Long id) {
        return repo.findById(id);
    }

    public Optional<usuarioModel> findByEmail(String email) {
        return repo.findByEmail(email);
    }
    

    public usuarioModel save(usuarioModel usuario) {
        return repo.save(usuario);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
