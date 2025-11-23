/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;


import Modelos.Tarifa;
import Modelos.ParqueoException.TarifaNoDefinidaException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTarifa {
    private List<Tarifa> tarifas;

    public RepositorioTarifa() {
        this.tarifas = new ArrayList<>();
    }

    public synchronized void guardar(Tarifa tarifa) {
        if (tarifa == null) {
            throw new IllegalArgumentException("La tarifa no puede ser nula");
        }
        tarifas.add(tarifa);
    }

    public Tarifa obtenerTarifa(String tipo) throws TarifaNoDefinidaException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new TarifaNoDefinidaException("El tipo de tarifa no puede estar vacio");
        }
        
        String tipoNormalizado = tipo.trim().toUpperCase();
        
        for (Tarifa t : tarifas) {
            if (t.getTipo().equalsIgnoreCase(tipoNormalizado)) {
                return t;
            }
        }
        throw new TarifaNoDefinidaException("Tarifa para tipo " + tipo + " no esta definida");
    }

    public synchronized void actualizar(Tarifa tarifa) throws TarifaNoDefinidaException {
        if (tarifa == null) {
            throw new IllegalArgumentException("La tarifa no puede ser nula");
        }
        
        for (int i = 0; i < tarifas.size(); i++) {
            if (tarifas.get(i).getTipo().equalsIgnoreCase(tarifa.getTipo())) {
                tarifas.set(i, tarifa);
                return;
            }
        }
        throw new TarifaNoDefinidaException("Tarifa para tipo " + tarifa.getTipo() + " no encontrada");
    }

    public List<Tarifa> listarTodos() {
        return new ArrayList<>(tarifas);
    }
}