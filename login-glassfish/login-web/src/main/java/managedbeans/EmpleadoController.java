package managedbeans;

import entities.Empleado;
import entities.Rol;
import entities.Usuario;
import managedbeans.util.JsfUtil;
import managedbeans.util.JsfUtil.PersistAction;
import sessionbeans.EmpleadoFacadeLocal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("empleadoController")
@SessionScoped
public class EmpleadoController implements Serializable {

    @EJB
    private sessionbeans.EmpleadoFacadeLocal ejbFacade;
    private List<Empleado> items = null;
    private Empleado selected;
    private List<String> roles;
    @Inject
    UsuarioController usrCtrl;
    @Inject
    private RolController rolCtrl;
    public EmpleadoController() {
    }

    public Empleado getSelected() {
        return selected;
    }

    public void setSelected(Empleado selected) {
        this.selected = selected;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EmpleadoFacadeLocal getFacade() {
        return ejbFacade;
    }

    public Empleado prepareCreate() {
        selected = new Empleado();
        initializeEmbeddableKey();
        return selected;
    }
    
    public Empleado prepareUpdate() {
        roles = new ArrayList<>();
        if (selected.getRoles()!=null && !selected.getRoles().isEmpty()) {
            for (Rol item : selected.getRoles()) {
                roles.add(""+item.getId());
            }
        }
        return selected;
    } 

    public void create() {
        for (String role : roles) {
            Rol rol = rolCtrl.getRol(Long.valueOf(role));
            selected.getRoles().add(rol);
            rol.getUsuarios().add(selected);
//            System.out.println(rol.getNombre());
        }
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EmpleadoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            usrCtrl.setItems(null);
            rolCtrl.setItems(null);
        }
    }

    public void update() {
        Usuario usuario = usrCtrl.getUsuario(selected.getId());
        List<String> noSelect = noSelectRoles(); 
        for (String item : roles) {
            Rol rol = rolCtrl.getRol(Long.valueOf(item));
            if (!selected.getRoles().contains(rol)) {
                selected.getRoles().add(rol);
                rol.getUsuarios().add(selected);
            }
        }
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EmpleadoUpdated"));
        for (String item : noSelect) {
            Rol rol = rolCtrl.getRol(Long.valueOf(item));
            if (selected.getRoles().contains(rol)) {
                selected.getRoles().remove(rol);
                rol.getUsuarios().remove(selected);
                rolCtrl.setSelected(rol);
                rolCtrl.update();
            }
        }
        usuario.setRoles(selected.getRoles());
        usrCtrl.setSelected(usuario);
        usrCtrl.update2();
        rolCtrl.setItems(null);
        usrCtrl.setItems(null);
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EmpleadoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Empleado> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public void setItems(List<Empleado> items) {
        this.items = items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Empleado getEmpleado(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Empleado> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Empleado> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    private List<String> noSelectRoles() {
        List<String> noSelect = new ArrayList<>();
        for (Rol rol : rolCtrl.getItemsAvailableSelectMany()) {
            if (!roles.contains(""+rol.getId())) {
                noSelect.add(""+rol.getId());
            }
        }
        return noSelect;
    }

    @FacesConverter(forClass = Empleado.class)
    public static class EmpleadoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmpleadoController controller = (EmpleadoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "empleadoController");
            return controller.getEmpleado(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Empleado) {
                Empleado o = (Empleado) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Empleado.class.getName()});
                return null;
            }
        }

    }

}
