/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author seba
 */
@Entity
@PrimaryKeyJoinColumn(name="usuario_codigo")
public class Empleado extends Usuario{

    @Column(name="empleado_rut")
    private String rut;

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
    
    
}
