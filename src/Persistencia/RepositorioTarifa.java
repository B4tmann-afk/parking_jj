package Persistencia;

import Modelos.Tarifa;
import Modelos.ParqueoException.TarifaNoDefinidaException;
import Modelos.ParqueoException.DatosInvalidosException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTarifa {
    private List<Tarifa> tarifas;
    
    public RepositorioTarifa() {
        this.tarifas = new ArrayList<>();
        inicializarTarifasDefecto();
    }
    
    /**
     * Inicializa las tarifas por defecto del sistema
     */
    private void inicializarTarifasDefecto() {
        try {
            // Tarifa para MOTO: $2000/hora, $20000/día
            Tarifa tarifaMoto = new Tarifa("MOTO", 2000, 20000);
            tarifas.add(tarifaMoto);
            
            // Tarifa para AUTO: $3000/hora, $30000/día
            Tarifa tarifaAuto = new Tarifa("AUTO", 3000, 30000);
            tarifas.add(tarifaAuto);
            
            System.out.println("✅ Tarifas inicializadas correctamente:");
            System.out.println("   - MOTO: $2000/hora, $20000/día");
            System.out.println("   - AUTO: $3000/hora, $30000/día");
            
        } catch (DatosInvalidosException e) {
            System.err.println("❌ Error al inicializar tarifas: " + e.getMessage());
        }
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