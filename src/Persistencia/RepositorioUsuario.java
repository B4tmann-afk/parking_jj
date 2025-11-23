/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;


import Modelos.Usuario;
import Modelos.ParqueoException.UsuarioNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuario {
    private List<Usuario> usuarios;

    public RepositorioUsuario() {
        this.usuarios = new ArrayList<>();
    }

    public synchronized void guardar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        usuarios.add(usuario);
    }

    public Usuario buscar(String usuario) throws UsuarioNoEncontradoException {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new UsuarioNoEncontradoException("El nombre de usuario no puede estar vacio");
        }
        
        String usuarioNormalizado = usuario.trim();
        
        for (Usuario u : usuarios) {
            if (u.getUsuario().equalsIgnoreCase(usuarioNormalizado)) {
                return u;
            }
        }
        throw new UsuarioNoEncontradoException("Usuario '" + usuario + "' no encontrado");
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }
}