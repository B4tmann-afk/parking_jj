/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;


import Modelos.ParqueoException.DatosInvalidosException;

public class Trabajador extends Usuario {
    private String turno;
    private double salario;

    public Trabajador(String id, String nombre, String cedula, String telefono, String usuario, String contraseña, String turno, double salario) throws DatosInvalidosException {
        super(id, nombre, cedula, telefono, usuario, contraseña);
        Validador.validarTextoNoVacio(turno, "turno");
        Validador.validarNumeroPositivo(salario, "salario");
        
        this.turno = turno.trim();
        this.salario = salario;
    }

    public String getTurno() {
        return turno;
    }

    public double getSalario() {
        return salario;
    }

    @Override
    public String toString() {
        return "Trabajador{nombre='" + getNombre() + "', usuario='" + getUsuario() + "', turno='" + turno + "'}";
    }
}

