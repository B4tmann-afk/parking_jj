/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;



import Modelos.Vehiculo;
import Modelos.Auto;
import Modelos.Moto;
import Modelos.Validador;
import Modelos.ParqueoException.VehiculoNoEncontradoException;
import Modelos.ParqueoException.PlacaDuplicadaException;
import Modelos.ParqueoException.DatosInvalidosException;
import Persistencia.RepositorioVehiculo;
import java.util.List;

public class ServicioVehiculo {
    private RepositorioVehiculo repo;

    public ServicioVehiculo(RepositorioVehiculo repo) {
        if (repo == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repo = repo;
    }

    public Vehiculo registrarAuto(String placa, String marca, String modelo) throws DatosInvalidosException, PlacaDuplicadaException {
        if (placa == null || marca == null || modelo == null) {
            throw new DatosInvalidosException("Los datos del vehiculo no pueden ser nulos");
        }
        
        placa = Validador.normalizarPlaca(placa);
        marca = marca.trim();
        modelo = modelo.trim();
        
        Auto auto = new Auto(placa, marca, modelo);
        repo.guardar(auto);
        return auto;
    }

    public Vehiculo registrarMoto(String placa, String marca, String modelo) throws DatosInvalidosException, PlacaDuplicadaException {
        if (placa == null || marca == null || modelo == null) {
            throw new DatosInvalidosException("Los datos del vehiculo no pueden ser nulos");
        }
        
        placa = Validador.normalizarPlaca(placa);
        marca = marca.trim();
        modelo = modelo.trim();
        
        Moto moto = new Moto(placa, marca, modelo);
        repo.guardar(moto);
        return moto;
    }

    public Vehiculo buscarVehiculo(String placa) throws VehiculoNoEncontradoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new VehiculoNoEncontradoException("La placa no puede estar vacia");
        }
        return repo.buscar(Validador.normalizarPlaca(placa));
    }

    public List<Vehiculo> listarTodosVehiculos() {
        return repo.listarTodos();
    }

    public boolean existeVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return false;
        }
        try {
            repo.buscar(Validador.normalizarPlaca(placa));
            return true;
        } catch (VehiculoNoEncontradoException e) {
            return false;
        }
    }

    public List<Vehiculo> listarVehiculos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
