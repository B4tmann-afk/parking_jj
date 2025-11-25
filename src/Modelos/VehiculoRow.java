package Modelos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VehiculoRow {

    private final StringProperty placa;
    private final StringProperty tipo;

    public VehiculoRow(String placa, String tipo) {
        this.placa = new SimpleStringProperty(placa);
        this.tipo = new SimpleStringProperty(tipo);
    }

    public StringProperty placaProperty() { return placa; }
    public StringProperty tipoProperty() { return tipo; }

    public void setPlaca(String p) { placa.set(p); }
    public void setTipo(String t) { tipo.set(t); }
}
