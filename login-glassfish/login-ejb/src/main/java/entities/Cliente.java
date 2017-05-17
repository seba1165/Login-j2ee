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
public class Cliente extends Usuario{
    
    @Column(name="cliente_contrato")
    private String contrato;

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }
    
    
}
