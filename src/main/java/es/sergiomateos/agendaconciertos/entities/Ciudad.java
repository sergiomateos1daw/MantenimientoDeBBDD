/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sergiomateos.agendaconciertos.entities;

import es.sergiomateos.agendaconciertos.entities.Artista;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author sergi
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "CIUDAD")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Ciudad.findAll", query = "SELECT c FROM Ciudad c"),
    @javax.persistence.NamedQuery(name = "Ciudad.findById", query = "SELECT c FROM Ciudad c WHERE c.id = :id"),
    @javax.persistence.NamedQuery(name = "Ciudad.findByCodigo", query = "SELECT c FROM Ciudad c WHERE c.codigo = :codigo"),
    @javax.persistence.NamedQuery(name = "Ciudad.findByNombre", query = "SELECT c FROM Ciudad c WHERE c.nombre = :nombre")})
public class Ciudad implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Column(name = "CODIGO")
    private String codigo;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.OneToMany(mappedBy = "ciudad")
    private Collection<Artista> artistaCollection;

    public Ciudad() {
    }

    public Ciudad(Integer id) {
        this.id = id;
    }

    public Ciudad(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Artista> getArtistaCollection() {
        return artistaCollection;
    }

    public void setArtistaCollection(Collection<Artista> artistaCollection) {
        this.artistaCollection = artistaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ciudad)) {
            return false;
        }
        Ciudad other = (Ciudad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.sergiomateos.agendaconciertos.entities.Ciudad[ id=" + id + " ]";
    }
    
}
