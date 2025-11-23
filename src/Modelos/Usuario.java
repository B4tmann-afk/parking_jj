/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;


import Modelos.ParqueoException.DatosInvalidosException;

public class Usuario extends Persona {
    private String usuario;
    private String contraseña;

    public Usuario(String id, String nombre, String cedula, String telefono, String usuario, String contraseña) throws DatosInvalidosException {
        super(id, nombre, cedula, telefono);
        Validador.validarUsuario(usuario);
        Validador.validarContraseña(contraseña);
        
        this.usuario = usuario.trim();
        this.contraseña = contraseña;
    }

    public String getUsuario() {
        return usuario;
    }

    public boolean validarCredenciales(String usuario, String contraseña) {
        if (usuario == null || contraseña == null) return false;
        return this.usuario.equalsIgnoreCase(usuario.trim()) && this.contraseña.equals(contraseña);
    }

    @Override
    public String toString() {
        return "Usuario{nombre='" + getNombre() + "', usuario='" + usuario + "'}";
    }
}