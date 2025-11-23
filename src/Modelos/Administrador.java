package Modelos;

import Modelos.ParqueoException.DatosInvalidosException;

public class Administrador extends Usuario {

    public Administrador(String id, String nombre, String cedula,
            String telefono, String usuario, String contraseña)
            throws DatosInvalidosException {

        super(id, nombre, cedula, telefono, usuario, contraseña, Rol.ADMIN);
    }
}
