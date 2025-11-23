/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Modelos.Usuario;
import Modelos.Administrador;
import Modelos.Trabajador;
import Modelos.ParqueoException.UsuarioNoEncontradoException;
import Modelos.ParqueoException.CredencialesInvalidasException;
import Modelos.ParqueoException.DatosInvalidosException;
import Persistencia.RepositorioUsuario;

public class ServicioAutenticacion {
    private RepositorioUsuario repo;
    private Usuario usuarioActual;

    public ServicioAutenticacion(RepositorioUsuario repo) {
        if (repo == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repo = repo;
        this.usuarioActual = null;
    }

    public Usuario iniciarSesion(String usuario, String contraseña) throws UsuarioNoEncontradoException, CredencialesInvalidasException {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new CredencialesInvalidasException("El usuario no puede estar vacio");
        }
        if (contraseña == null || contraseña.isEmpty()) {
            throw new CredencialesInvalidasException("La contraseña no puede estar vacia");
        }
        
        Usuario user = repo.buscar(usuario.trim());
        
        if (!user.validarCredenciales(usuario.trim(), contraseña)) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }
        
        usuarioActual = user;
        return user;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public boolean estaAutenticado() {
        return usuarioActual != null;
    }

    public boolean esAdministrador() {
        return usuarioActual instanceof Administrador;
    }

    public boolean esTrabajador() {
        return usuarioActual instanceof Trabajador;
    }

    public void registrarAdministrador(String id, String nombre, String cedula, String telefono, String usuario, String contraseña) throws DatosInvalidosException, UsuarioNoEncontradoException {
        if (id == null || nombre == null || cedula == null || telefono == null || usuario == null || contraseña == null) {
            throw new DatosInvalidosException("Ningun campo puede ser nulo");
        }
        
        id = id.trim();
        nombre = nombre.trim();
        cedula = cedula.trim();
        telefono = telefono.trim();
        usuario = usuario.trim();
        
        try {
            repo.buscar(usuario);
            throw new DatosInvalidosException("El usuario '" + usuario + "' ya existe");
        } catch (UsuarioNoEncontradoException e) {
            // No existe, se puede crear
        }
        
        Administrador admin = new Administrador(id, nombre, cedula, telefono, usuario, contraseña);
        repo.guardar(admin);
    }

    public void registrarTrabajador(String id, String nombre, String cedula, String telefono, String usuario, String contraseña, String turno, double salario) throws DatosInvalidosException, UsuarioNoEncontradoException {
        if (id == null || nombre == null || cedula == null || telefono == null || usuario == null || contraseña == null || turno == null) {
            throw new DatosInvalidosException("Ningun campo puede ser nulo");
        }
        
        id = id.trim();
        nombre = nombre.trim();
        cedula = cedula.trim();
        telefono = telefono.trim();
        usuario = usuario.trim();
        turno = turno.trim();
        
        try {
            repo.buscar(usuario);
            throw new DatosInvalidosException("El usuario '" + usuario + "' ya existe");
        } catch (UsuarioNoEncontradoException e) {
            // No existe, se puede crear
        }
        
        Trabajador trabajador = new Trabajador(id, nombre, cedula, telefono, usuario, contraseña, turno, salario);
        repo.guardar(trabajador);
    }

    public Usuario login(String usuario, String contraseña) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}