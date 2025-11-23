package Modelos;

import Modelos.ParqueoException.DatosInvalidosException;

public class Cliente extends Usuario {

    public Cliente(String id, String nombre, String cedula,
            String telefono, String usuario, String contraseña)
            throws DatosInvalidosException {

        super(id, nombre, cedula, telefono, usuario, contraseña, Rol.CLIENTE);
    }
}
