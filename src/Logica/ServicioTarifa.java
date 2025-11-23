/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;


import Modelos.Tarifa;
import Modelos.ParqueoException;
import Modelos.ParqueoException.TarifaNoDefinidaException;
import Modelos.ParqueoException.DatosInvalidosException;
import Persistencia.RepositorioTarifa;
import java.util.List;

public class ServicioTarifa {
    private RepositorioTarifa repo;

    public ServicioTarifa(RepositorioTarifa repo) {
        if (repo == null) {
            throw new IllegalArgumentException("El repositorio no puede ser nulo");
        }
        this.repo = repo;
    }


    public void crearTarifa(String tipo, double valorHora, double valorDia) throws DatosInvalidosException, TarifaNoDefinidaException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new DatosInvalidosException("El tipo de tarifa no puede estar vacio");
        }
        
        tipo = tipo.trim().toUpperCase();
        
        try {
            repo.obtenerTarifa(tipo);
            throw new DatosInvalidosException("Ya existe una tarifa para el tipo " + tipo);
        } catch (TarifaNoDefinidaException e) {
            // No existe, se puede crear
        }
        
        Tarifa tarifa = new Tarifa(tipo, valorHora, valorDia);
        repo.guardar(tarifa);
    }

    public void actualizarTarifa(String tipo, double valorHora, double valorDia) throws TarifaNoDefinidaException, DatosInvalidosException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new DatosInvalidosException("El tipo de tarifa no puede estar vacio");
        }
        
        tipo = tipo.trim().toUpperCase();
        Tarifa tarifaExistente = repo.obtenerTarifa(tipo);
        Tarifa tarifaNueva = new Tarifa(tipo, valorHora, valorDia);
        repo.actualizar(tarifaNueva);
    }

    public Tarifa obtenerTarifa(String tipo) throws TarifaNoDefinidaException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new TarifaNoDefinidaException("El tipo de tarifa no puede estar vacio");
        }
        return repo.obtenerTarifa(tipo.trim().toUpperCase());
    }

    public List<Tarifa> listarTarifas() {
        return repo.listarTodos();
    }

    public double calcularCosto(String tipo, long minutos) throws TarifaNoDefinidaException {
        Tarifa tarifa = obtenerTarifa(tipo);
        return tarifa.calcular(minutos);
    }
}