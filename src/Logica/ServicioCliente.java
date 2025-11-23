/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

/**
 *
 * @author 2jcue
 */

import Modelos.Cliente;
import Modelos.ParqueoException;
import Persistencia.RepositorioCliente;
import java.util.List;

public class ServicioCliente {
    private final RepositorioCliente repo;

    public ServicioCliente(RepositorioCliente repo) {
        this.repo = repo;
    }

    public void registrarCliente(Cliente cliente) throws ParqueoException {
        repo.guardar(cliente);
        System.out.println(" Cliente " + cliente.getNombre() + " registrado exitosamente");
    }

    public Cliente buscarCliente(String id) throws ParqueoException {
        return repo.buscar(id);
    }

    public List<Cliente> listarClientes() {
        return repo.listarTodos();
    }
}

