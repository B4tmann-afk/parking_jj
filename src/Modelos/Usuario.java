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
    
    // ============ GETTERS ============
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
    
    public String getUsuario() {
        return usuario;
    }
    
    public String getContraseña() {
        return contraseña;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    // ============ SETTERS ============
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", usuario='" + usuario + '\'' +
                ", rol=" + rol +
                '}';
    }
}