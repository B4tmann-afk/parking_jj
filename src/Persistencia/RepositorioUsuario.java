package Persistencia;

import Modelos.Usuario;
import Modelos.ParqueoException.UsuarioNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuario {

    private static final List<Usuario> usuarios = new ArrayList<>();

    public static synchronized void guardar(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Usuario nulo");
        usuarios.add(usuario);
    }

    public static Usuario buscar(String user) throws UsuarioNoEncontradoException {
        if (user == null || user.trim().isEmpty())
            throw new UsuarioNoEncontradoException("Usuario vacío");

        String u = user.trim();

        return usuarios.stream()
                .filter(x -> x.getUsuario().equalsIgnoreCase(u))
                .findFirst()
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario '" + user + "' no encontrado"));
    }

    public static List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }
}
