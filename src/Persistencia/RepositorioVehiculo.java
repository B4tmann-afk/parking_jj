/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;


import Modelos.Vehiculo;
import Modelos.Validador;
import Modelos.ParqueoException.VehiculoNoEncontradoException;
import Modelos.ParqueoException.PlacaDuplicadaException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioVehiculo {
    private List<Vehiculo> vehiculos;

    public RepositorioVehiculo() {
        this.vehiculos = new ArrayList<>();
    }

    public synchronized void guardar(Vehiculo vehiculo) throws PlacaDuplicadaException {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehiculo no puede ser nulo");
        }
        
        String placaNormalizada = Validador.normalizarPlaca(vehiculo.getPlaca());
        
        for (Vehiculo v : vehiculos) {
            if (Validador.normalizarPlaca(v.getPlaca()).equalsIgnoreCase(placaNormalizada)) {
                throw new PlacaDuplicadaException("La placa " + vehiculo.getPlaca() + " ya esta registrada");
            }
        }
        vehiculos.add(vehiculo);
    }

    public Vehiculo buscar(String placa) throws VehiculoNoEncontradoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new VehiculoNoEncontradoException("La placa no puede estar vacia");
        }
        
        String placaNormalizada = Validador.normalizarPlaca(placa);
        
        for (Vehiculo v : vehiculos) {
            if (Validador.normalizarPlaca(v.getPlaca()).equalsIgnoreCase(placaNormalizada)) {
                return v;
            }
        }
        throw new VehiculoNoEncontradoException("Vehiculo con placa " + placa + " no encontrado");
    }

    public List<Vehiculo> listarTodos() {
        return new ArrayList<>(vehiculos);
    }
}