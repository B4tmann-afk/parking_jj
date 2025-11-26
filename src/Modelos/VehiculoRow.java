package Modelos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VehiculoRow {
    private final StringProperty placa;
    private final StringProperty tipo;
    private final StringProperty marca;
    private final StringProperty modelo;
    
    public VehiculoRow(String placa, String tipo, String marca, String modelo) {
        this.placa = new SimpleStringProperty(placa);
        this.tipo = new SimpleStringProperty(tipo);
        this.marca = new SimpleStringProperty(marca);
        this.modelo = new SimpleStringProperty(modelo);
    }
    
    // Getters de propiedades (para TableView)
    public StringProperty placaProperty() { 
        return placa; 
    }
    
    public StringProperty tipoProperty() { 
        return tipo; 
    }
    
    public StringProperty marcaProperty() { 
        return marca; 
    }
    
    public StringProperty modeloProperty() { 
        return modelo; 
    }
    
    // Setters
    public void setPlaca(String p) { 
        placa.set(p); 
    }
    
    public void setTipo(String t) { 
        tipo.set(t); 
    }
    
    public void setMarca(String m) { 
        marca.set(m); 
    }
    
    public void setModelo(String m) { 
        modelo.set(m); 
    }
    
    // Getters simples (opcional)
    public String getPlaca() { 
        return placa.get(); 
    }
    
    public String getTipo() { 
        return tipo.get(); 
    }
    
    public String getMarca() { 
        return marca.get(); 
    }
    
    public String getModelo() { 
        return modelo.get(); 
    }
}