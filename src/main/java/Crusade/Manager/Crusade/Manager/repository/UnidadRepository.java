package Crusade.Manager.Crusade.Manager.repository;

import Crusade.Manager.Crusade.Manager.model.Unidad;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UnidadRepository extends MongoRepository<Unidad, String> {

    List<Unidad> findByRosterId(String rosterId);
}
