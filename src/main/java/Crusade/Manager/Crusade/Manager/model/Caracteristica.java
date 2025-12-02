package Crusade.Manager.Crusade.Manager.model;

import java.util.List;

public class Caracteristica {

    private String id;
    private String nombre;
    private String stat;

    // lista de modificadores anidados
    private List<Modificador> modificadores;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getStat() { return stat; }
    public void setStat(String stat) { this.stat = stat; }

    public List<Modificador> getModificadores() { return modificadores; }
    public void setModificadores(List<Modificador> modificadores) { this.modificadores = modificadores; }
}
