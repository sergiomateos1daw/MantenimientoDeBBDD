/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sergiomateos.agendaconciertos.entities;

import es.sergiomateos.agendaconciertos.entities.Ciudad;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author sergi
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "ARTISTA")
@javax.persistence.NamedQueries({
    @javax.persistence.NamedQuery(name = "Artista.findAll", query = "SELECT a FROM Artista a"),
    @javax.persistence.NamedQuery(name = "Artista.findById", query = "SELECT a FROM Artista a WHERE a.id = :id"),
    @javax.persistence.NamedQuery(name = "Artista.findByNombre", query = "SELECT a FROM Artista a WHERE a.nombre = :nombre"),
    @javax.persistence.NamedQuery(name = "Artista.findByApellidos", query = "SELECT a FROM Artista a WHERE a.apellidos = :apellidos"),
    @javax.persistence.NamedQuery(name = "Artista.findByTelefono", query = "SELECT a FROM Artista a WHERE a.telefono = :telefono"),
    @javax.persistence.NamedQuery(name = "Artista.findByEmail", query = "SELECT a FROM Artista a WHERE a.email = :email"),
    @javax.persistence.NamedQuery(name = "Artista.findByFechaNacimiento", query = "SELECT a FROM Artista a WHERE a.fechaNacimiento = :fechaNacimiento"),
    @javax.persistence.NamedQuery(name = "Artista.findBySalario", query = "SELECT a FROM Artista a WHERE a.salario = :salario"),
    @javax.persistence.NamedQuery(name = "Artista.findByFoto", query = "SELECT a FROM Artista a WHERE a.foto = :foto")})
public class Artista implements Serializable {

    private static final long serialVersionUID = 1L;
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "ID")
    private Integer id;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "NOMBRE")
    private String nombre;
    @javax.persistence.Basic(optional = false)
    @javax.persistence.Column(name = "APELLIDOS")
    private String apellidos;
    @javax.persistence.Column(name = "TELEFONO")
    private String telefono;
    @javax.persistence.Column(name = "EMAIL")
    private String email;
    @javax.persistence.Column(name = "FECHA_NACIMIENTO")
    @javax.persistence.Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNacimiento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @javax.persistence.Column(name = "SALARIO")
    private BigDecimal salario;
    @javax.persistence.Column(name = "FOTO")
    private String foto;
    @javax.persistence.JoinColumn(name = "CIUDAD", referencedColumnName = "ID")
    @javax.persistence.ManyToOne
    private Ciudad ciudad;

    public Artista() {
    }

    public Artista(Integer id) {
        this.id = id;
    }

    public Artista(Integer id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
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
        if (!(object instanceof Artista)) {
            return false;
        }
        Artista other = (Artista) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "es.sergiomateos.agendaconciertos.entities.Artista[ id=" + id + " ]";
    }
    
}
