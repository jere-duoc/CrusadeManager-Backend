package Crusade.Manager.Crusade.Manager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "unidades")
public class Unidad {

    @Id
    private String id;

    private String nombre;
    private List<String> keywords;
    private int puntos;

    // Id del roster al cual pertenece
    private String rosterId;

    // Lista de características anidadas
    private List<Caracteristica> caracteristicas;

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }

    public int getPuntos() { return puntos; }
    public void setPuntos(int puntos) { this.puntos = puntos; }

    public String getRosterId() { return rosterId; }
    public void setRosterId(String rosterId) { this.rosterId = rosterId; }

    public List<Caracteristica> getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(List<Caracteristica> caracteristicas) { this.caracteristicas = caracteristicas; }
}
