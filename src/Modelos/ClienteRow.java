package Modelos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClienteRow {
    private final StringProperty cedula;
    private final StringProperty nombre;
    private final StringProperty telefono;
    private final StringProperty usuario;
    
    public ClienteRow(String cedula, String nombre, String telefono, String usuario) {
        this.cedula = new SimpleStringProperty(cedula);
        this.nombre = new SimpleStringProperty(nombre);
        this.telefono = new SimpleStringProperty(telefono);
        this.usuario = new SimpleStringProperty(usuario);
    }
    
    // Getters de propiedades (para TableView)
    public StringProperty cedulaProperty() { 
        return cedula; 
    }
    
    public StringProperty nombreProperty() { 
        return nombre; 
    }
    
    public StringProperty telefonoProperty() { 
        return telefono; 
    }
    
    public StringProperty usuarioProperty() { 
        return usuario; 
    }
    
    // Setters
    public void setCedula(String c) { 
        cedula.set(c); 
    }
    
    public void setNombre(String n) { 
        nombre.set(n); 
    }
    
    public void setTelefono(String t) { 
        telefono.set(t); 
    }
    
    public void setUsuario(String u) { 
        usuario.set(u); 
    }
    
    // Getters simples (opcional)
    public String getCedula() { 
        return cedula.get(); 
    }
    
    public String getNombre() { 
        return nombre.get(); 
    }
    
    public String getTelefono() { 
        return telefono.get(); 
    }
    
    public String getUsuario() { 
        return usuario.get(); 
    }
}