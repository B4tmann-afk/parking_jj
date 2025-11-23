/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;


import Modelos.ParqueoException.DatosInvalidosException;

public class Administrador extends Usuario {
    public Administrador(String id, String nombre, String cedula, String telefono, String usuario, String contraseña) throws DatosInvalidosException {
        super(id, nombre, cedula, telefono, usuario, contraseña);
    }

    @Override
    public String toString() {
        return "Administrador{nombre='" + getNombre() + "', usuario='" + getUsuario() + "'}";
    }
}