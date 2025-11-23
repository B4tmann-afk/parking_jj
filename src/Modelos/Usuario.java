package Modelos;

import Modelos.ParqueoException.DatosInvalidosException;

public class Usuario {

    private String id;
    private String nombre;
    private String cedula;
    private String telefono;
    private String usuario;
    private String contraseña;
    private Rol rol;

    public Usuario(String id, String nombre, String cedula, String telefono,
            String usuario, String contraseña, Rol rol) throws DatosInvalidosException {

        if (usuario == null || usuario.trim().isEmpty()) {
            throw new DatosInvalidosException("Usuario no puede estar vacío");
        }
        if (contraseña == null || contraseña.isEmpty()) {
            throw new DatosInvalidosException("Contraseña no válida");
        }

        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public boolean validarCredenciales(String user, String pass) {
        return usuario.equals(user) && contraseña.equals(pass);
    }

    public Rol getRol() {
        return rol;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }
}
