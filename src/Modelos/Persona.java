/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import Modelos.ParqueoException.DatosInvalidosException;

public abstract class Persona {
    private String id;
    private String nombre;
    private String cedula;
    private String telefono;

    public Persona(String id, String nombre, String cedula, String telefono) throws DatosInvalidosException {
        Validador.validarTextoNoVacio(id, "ID");
        Validador.validarTextoNoVacio(nombre, "nombre");
        Validador.validarCedula(cedula);
        Validador.validarTelefono(telefono);
        
        this.id = id.trim();
        this.nombre = nombre.trim();
        this.cedula = cedula.trim().replaceAll("\\s+", "");
        this.telefono = telefono.trim().replaceAll("\\s+", "");
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getTelefono() {
        return telefono;
    }
}
