package Crusade.Manager.Crusade.Manager.service;

import Crusade.Manager.Crusade.Manager.model.Unidad;
import Crusade.Manager.Crusade.Manager.repository.UnidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadService {

    private final UnidadRepository repository;

    public UnidadService(UnidadRepository repository) {
        this.repository = repository;
    }

    public List<Unidad> findAll() {
        return repository.findAll();
    }

    public Unidad findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Unidad save(Unidad unidad) {
        return repository.save(unidad);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Unidad> findByRosterId(String rosterId) {
        return repository.findByRosterId(rosterId);
    }
}
