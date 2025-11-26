package Persistencia;

import Modelos.Vehiculo;
import Modelos.ParqueoException.VehiculoNoEncontradoException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioVehiculo {
    private List<Vehiculo> vehiculos;
    
    public RepositorioVehiculo() {
        this.vehiculos = new ArrayList<>();
    }
    
    public synchronized void guardar(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo no puede ser nulo");
        }
        vehiculos.add(vehiculo);
    }
    
    public Vehiculo buscar(String placa) throws VehiculoNoEncontradoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new VehiculoNoEncontradoException("La placa no puede estar vacia");
        }
        
        String placaNormalizada = placa.trim().toUpperCase();
        
        for (Vehiculo v : vehiculos) {
            if (v.getPlaca().equalsIgnoreCase(placaNormalizada)) {
                return v;
            }
        }
        throw new VehiculoNoEncontradoException("Vehiculo con placa " + placa + " no encontrado");
    }
    
    public synchronized void actualizar(Vehiculo vehiculo, String placaOriginal) throws VehiculoNoEncontradoException {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo no puede ser nulo");
        }
        
        for (int i = 0; i < vehiculos.size(); i++) {
            if (vehiculos.get(i).getPlaca().equalsIgnoreCase(placaOriginal)) {
                vehiculos.set(i, vehiculo);
                return;
            }
        }
        throw new VehiculoNoEncontradoException("Vehiculo con placa " + placaOriginal + " no encontrado");
    }
    
    public synchronized void eliminar(String placa) throws VehiculoNoEncontradoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new VehiculoNoEncontradoException("La placa no puede estar vacia");
        }
        
        for (int i = 0; i < vehiculos.size(); i++) {
            if (vehiculos.get(i).getPlaca().equalsIgnoreCase(placa)) {
                vehiculos.remove(i);
                return;
            }
        }
        throw new VehiculoNoEncontradoException("Vehiculo con placa " + placa + " no encontrado");
    }
    
    public List<Vehiculo> listarTodos() {
        return new ArrayList<>(vehiculos);
    }
    
    public List<Vehiculo> buscarPorTipo(String tipo) {
        List<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(v);
            }
        }
        return resultado;
    }
}