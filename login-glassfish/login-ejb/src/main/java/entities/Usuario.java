/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

/**
 *
 * @author seba
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="usuario_codigo")
    private Long id;
    
    @Column(name="usuario_nombre")
    private String nombre;
    
    @Column(name="usuario_correo", unique = true)
    private String correo;
    
    @Column(name="usuario_password")
    private String password;
    
    @ManyToMany(targetEntity=Rol.class, cascade = CascadeType.ALL)
    private List<Rol> roles;  

    public Usuario(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
        this.roles =  new ArrayList<>();
    }

    public Usuario() {
        this.roles = new ArrayList<>();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = this.sha256(password);
    }
    
    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.usuario[ id=" + id + " ]";
    }
    
    public String mostrarRoles(){
        String roles2="";
        for (Rol role : roles) {
            if (roles.indexOf(role)==roles.size()-1) {
                roles2 = roles2 + role.getNombre()+".";
            }else{
                roles2 = roles2+ " "+role.getNombre()+", ";
            }
        }
        return roles2;
    }

    public String sha256(String base){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            
            for(int i =0 ;i<hash.length;i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
}
