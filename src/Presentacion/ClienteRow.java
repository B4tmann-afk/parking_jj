/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClienteRow {

    private final StringProperty cedula;
    private final StringProperty nombre;

    public ClienteRow(String cedula, String nombre) {
        this.cedula = new SimpleStringProperty(cedula);
        this.nombre = new SimpleStringProperty(nombre);
    }

    public StringProperty cedulaProperty() { return cedula; }
    public StringProperty nombreProperty() { return nombre; }

    public void setCedula(String c) { cedula.set(c); }
    public void setNombre(String n) { nombre.set(n); }
}
