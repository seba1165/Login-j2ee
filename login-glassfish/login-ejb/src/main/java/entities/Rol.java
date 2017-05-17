/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author seba
 */
@Entity
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rol_codigo")
    private Long id;
    
    @Column(name="rol_nombre")
    private String nombre;
    
    @ManyToMany(targetEntity = Usuario.class, mappedBy = "roles")
    private List <Usuario> usuarios;

    public Rol() {
        this.usuarios = new ArrayList<>();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
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
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.roles[ nombre=" + nombre + " ]";
    }
    
    public String mostrarUsuarios(){
        String usuarios2="";
        for (Usuario usuario : usuarios) {
            if (usuarios.indexOf(usuario)==usuarios.size()-1) {
                usuarios2 = usuarios2 + usuario.getNombre()+".";
            }else{
                usuarios2 = usuarios2+ " "+usuario.getNombre()+", ";
            }
        }
        return usuarios2;
    }
    
}
