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
    
    public synchronized void actualizar(Cliente cliente) throws ClienteNoEncontradoException {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId().equalsIgnoreCase(cliente.getId())) {
                clientes.set(i, cliente);
                return;
            }
        }
        throw new ClienteNoEncontradoException("Cliente con ID " + cliente.getId() + " no encontrado");
    }
    
    public synchronized void eliminar(String id) throws ClienteNoEncontradoException {
        if (id == null || id.trim().isEmpty()) {
            throw new ClienteNoEncontradoException("El ID del cliente no puede estar vacio");
        }
        
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getId().equalsIgnoreCase(id)) {
                clientes.remove(i);
                return;
            }
        }
        throw new ClienteNoEncontradoException("Cliente con ID " + id + " no encontrado");
    }
    
    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }
}