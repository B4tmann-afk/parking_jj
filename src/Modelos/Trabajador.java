package Modelos;

import Modelos.ParqueoException.DatosInvalidosException;

public class Trabajador extends Usuario {

    private String turno;
    private double salario;

    public Trabajador(String id, String nombre, String cedula, String telefono,
            String usuario, String contraseña,
            String turno, double salario) throws DatosInvalidosException {

        super(id, nombre, cedula, telefono, usuario, contraseña, Rol.TRABAJADOR);
        this.turno = turno;
        this.salario = salario;
    }
}
