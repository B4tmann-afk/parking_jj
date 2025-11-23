/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Modelos.Cliente;
import Modelos.ParqueoException.ClienteNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCliente {
    private List<Cliente> clientes;

    public RepositorioCliente() {
        this.clientes = new ArrayList<>();
    }

    public synchronized void guardar(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        clientes.add(cliente);
    }

    public Cliente buscar(String id) throws ClienteNoEncontradoException {
        if (id == null || id.trim().isEmpty()) {
            throw new ClienteNoEncontradoException("El ID del cliente no puede estar vacio");
        }
        
        String idNormalizado = id.trim();
        
        for (Cliente c : clientes) {
            if (c.getId().equalsIgnoreCase(idNormalizado)) {
                return c;
            }
        }
        throw new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado");
    }

    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }
}
