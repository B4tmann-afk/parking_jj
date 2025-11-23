/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;


import Modelos.ParqueoException.DatosInvalidosException;

public class Cliente extends Persona {
    private String email;
    private String direccion;

    public Cliente(String id, String nombre, String cedula, String telefono, String email, String direccion) throws DatosInvalidosException {
        super(id, nombre, cedula, telefono);
        Validador.validarEmail(email);
        Validador.validarTextoNoVacio(direccion, "direccion");
        
        this.email = email.trim().toLowerCase();
        this.direccion = direccion.trim();
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Cliente{nombre='" + getNombre() + "', email='" + email + "'}";
    }
}