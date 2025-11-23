/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;


import Modelos.Vehiculo;
// <- se mantiene aunque ya no se use
import Modelos.Registro;
import Modelos.Tarifa;
import Modelos.Validador;
import Modelos.ParqueoException;
import Modelos.ParqueoException.PlacaDuplicadaException;
import Modelos.ParqueoException.DatosInvalidosException;
import Persistencia.RepositorioVehiculo;
import Persistencia.RepositorioRegistro;
import Persistencia.RepositorioTarifa;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServicioParqueo {
    private RepositorioVehiculo repoVehiculo;
    private RepositorioRegistro repoRegistro;
    private RepositorioTarifa repoTarifa;

    public ServicioParqueo(RepositorioVehiculo repoVehiculo, RepositorioRegistro repoRegistro, RepositorioTarifa repoTarifa) {
        if (repoVehiculo == null || repoRegistro == null || repoTarifa == null) {
            throw new IllegalArgumentException("Los repositorios no pueden ser nulos");
        }
        this.repoVehiculo = repoVehiculo;
        this.repoRegistro = repoRegistro;
        this.repoTarifa = repoTarifa;
    }

    public synchronized void ingresarVehiculo(Vehiculo vehiculo) throws ParqueoException {
        if (vehiculo == null) {
            throw new DatosInvalidosException("El vehiculo no puede ser nulo");
        }
        
        String placaNormalizada = Validador.normalizarPlaca(vehiculo.getPlaca());
        List<Registro> registros = repoRegistro.listarRegistros();
        
        for (Registro r : registros) {
            String placaRegistro = Validador.normalizarPlaca(r.getVehiculo().getPlaca());
            if (placaRegistro.equals(placaNormalizada) && r.getFechaSalida() == null) {
                throw new ParqueoException("El vehiculo " + vehiculo.getPlaca() + " ya esta en el parqueo");
            }
        }
        
        try {
            repoVehiculo.guardar(vehiculo);
        } catch (PlacaDuplicadaException e) {
            // Ignorar si ya existe
        }
        
        Registro registro = new Registro(vehiculo);
        repoRegistro.guardar(registro);
        
        System.out.println(" Vehiculo " + vehiculo.getPlaca() + " ingreso al parqueo - Registro #" + registro.getId());
    }

    public synchronized double salidaVehiculo(String placa) throws ParqueoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new DatosInvalidosException("La placa no puede estar vacia");
        }
        
        Vehiculo vehiculo = repoVehiculo.buscar(placa);
        List<Registro> registros = repoRegistro.listarRegistros();
        Registro registroActual = null;
        
        String placaNormalizada = Validador.normalizarPlaca(placa);
        for (Registro r : registros) {
            String placaRegistro = Validador.normalizarPlaca(r.getVehiculo().getPlaca());
            if (placaRegistro.equals(placaNormalizada) && r.getFechaSalida() == null) {
                registroActual = r;
                break;
            }
        }
        
        if (registroActual == null) {
            throw new ParqueoException("El vehiculo " + placa + " no tiene registro activo");
        }
        
        registroActual.setFechaSalida(LocalDateTime.now());
        long minutos = registroActual.calcularMinutos();
        
        if (minutos < 1) {
            minutos = 1;
        }
        
        Tarifa tarifa = repoTarifa.obtenerTarifa(vehiculo.getTipo());
        double costo = tarifa.calcular(minutos);
        registroActual.setCosto(costo);
        repoRegistro.actualizar(registroActual);
        
        System.out.println(" Vehiculo " + placa + " salio del parqueo");
        System.out.println("  Tiempo: " + minutos + " minutos - Costo: $" + String.format("%.2f", costo));
        
        return costo;
    }

    public List<Registro> obtenerRegistros() {
        return repoRegistro.listarRegistros();
    }
    
    public List<Registro> obtenerRegistrosActivos() {
        List<Registro> activos = new ArrayList<>();
        for (Registro r : repoRegistro.listarRegistros()) {
            if (r.getFechaSalida() == null) {
                activos.add(r);
            }
        }
        return activos;
    }
}