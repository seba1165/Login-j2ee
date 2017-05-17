/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author seba
 */
@ManagedBean(name="loginBean")
@RequestScoped
public class LoginBean {
    private String username;
    private String password;
 
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
 
    public void login(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            request.login(username, password);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("",
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials, try again", ""));
        }
    }
 
    public String logout() throws ServletException, IOException{
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().invalidate(); 
        request.logout();
        return "/faces/index.xhtml?faces-redirect=true";
    }
 
    public String getLoggedUser(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if(request.getUserPrincipal() != null)
        return request.getUserPrincipal().getName();
        return "";
    }
}
