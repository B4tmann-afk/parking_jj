package Logica;

import Modelos.Administrador;
import Modelos.Trabajador;
import Persistencia.RepositorioUsuario;
import Modelos.ParqueoException.DatosInvalidosException;

public class DatosIniciales {

    public static void cargarUsuarios() {
        try {
            RepositorioUsuario.guardar(
                new Administrador("1","Jainer", "12345680", "3206708989", "admin", "123456")
            );
            RepositorioUsuario.guardar(
                new Trabajador("2", "Jose", "1067602079", "3216079009", "jose", "123456", "Dia", 2000)
            );
            System.out.println("Usuarios iniciales cargados.");
        } catch (DatosInvalidosException e) {
            System.err.println("Error cargando usuarios: " + e.getMessage());
        }
    }
}
