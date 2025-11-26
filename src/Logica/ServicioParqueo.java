package Logica;

import Modelos.Vehiculo;
import Modelos.Registro;
import Modelos.Tarifa;
import Modelos.Validador;
import Modelos.ParqueoException;
import Modelos.ParqueoException.DatosInvalidosException;
import Modelos.ParqueoException.VehiculoNoEncontradoException;
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

    /**
     * Registra el ingreso de un vehículo al parqueadero
     */
    public synchronized void ingresarVehiculo(Vehiculo vehiculo) throws ParqueoException {
        if (vehiculo == null) {
            throw new DatosInvalidosException("El vehiculo no puede ser nulo");
        }
        
        String placaNormalizada = Validador.normalizarPlaca(vehiculo.getPlaca());
        
        // Verificar si el vehículo ya está en el parqueadero
        if (estaEnParqueadero(placaNormalizada)) {
            throw new ParqueoException("El vehiculo " + vehiculo.getPlaca() + " ya esta en el parqueadero");
        }
        
        // Verificar si el vehículo ya existe en el sistema
        try {
            Vehiculo vehiculoExistente = repoVehiculo.buscar(placaNormalizada);
            // Si existe, usar el vehículo existente
            vehiculo = vehiculoExistente;
        } catch (VehiculoNoEncontradoException e) {
            // No existe, guardar el nuevo vehículo
            repoVehiculo.guardar(vehiculo);
        }
        
        // Crear registro de entrada
        Registro registro = new Registro(vehiculo);
        repoRegistro.guardar(registro);
        
        System.out.println("✅ Vehiculo " + vehiculo.getPlaca() + " ingreso al parqueo - Registro #" + registro.getId());
    }

    /**
     * Registra la salida de un vehículo y calcula el costo
     */
    public synchronized double salidaVehiculo(String placa) throws ParqueoException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new DatosInvalidosException("La placa no puede estar vacia");
        }
        
        String placaNormalizada = Validador.normalizarPlaca(placa);
        
        // Buscar vehículo
        Vehiculo vehiculo;
        try {
            vehiculo = repoVehiculo.buscar(placaNormalizada);
        } catch (VehiculoNoEncontradoException e) {
            throw new ParqueoException("Vehiculo con placa " + placa + " no encontrado en el sistema");
        }
        
        // Buscar registro activo
        Registro registroActual = buscarRegistroActivo(placaNormalizada);
        
        if (registroActual == null) {
            throw new ParqueoException("El vehiculo " + placa + " no tiene registro activo en el parqueadero");
        }
        
        // Registrar salida
        registroActual.setFechaSalida(LocalDateTime.now());
        long minutos = registroActual.calcularMinutos();
        
        // Mínimo 1 minuto
        if (minutos < 1) {
            minutos = 1;
        }
        
        // Calcular costo según la tarifa
        Tarifa tarifa = repoTarifa.obtenerTarifa(vehiculo.getTipo());
        double costo = tarifa.calcular(minutos);
        registroActual.setCosto(costo);
        
        // Actualizar registro
        repoRegistro.actualizar(registroActual);
        
        System.out.println("✅ Vehiculo " + placa + " salio del parqueadero");
        System.out.println("   Tiempo: " + minutos + " minutos - Costo: $" + String.format("%.0f", costo));
        
        return costo;
    }

    /**
     * Verifica si un vehículo está actualmente en el parqueadero
     */
    public boolean estaEnParqueadero(String placa) {
        String placaNormalizada = Validador.normalizarPlaca(placa);
        
        for (Registro r : repoRegistro.listarRegistros()) {
            String placaRegistro = Validador.normalizarPlaca(r.getVehiculo().getPlaca());
            if (placaRegistro.equals(placaNormalizada) && r.getFechaSalida() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca el registro activo de un vehículo
     */
    private Registro buscarRegistroActivo(String placa) {
        String placaNormalizada = Validador.normalizarPlaca(placa);
        
        for (Registro r : repoRegistro.listarRegistros()) {
            String placaRegistro = Validador.normalizarPlaca(r.getVehiculo().getPlaca());
            if (placaRegistro.equals(placaNormalizada) && r.getFechaSalida() == null) {
                return r;
            }
        }
        return null;
    }

    /**
     * Obtiene todos los registros (histórico completo)
     */
    public List<Registro> obtenerRegistros() {
        return repoRegistro.listarRegistros();
    }
    
    /**
     * Obtiene solo los registros activos (vehículos actualmente en el parqueadero)
     */
    public List<Registro> obtenerRegistrosActivos() {
        List<Registro> activos = new ArrayList<>();
        for (Registro r : repoRegistro.listarRegistros()) {
            if (r.getFechaSalida() == null) {
                activos.add(r);
            }
        }
        return activos;
    }
    
    /**
     * Obtiene el total de vehículos actualmente en el parqueadero
     */
    public int contarVehiculosActivos() {
        return obtenerRegistrosActivos().size();
    }
    
    /**
     * Obtiene el total de vehículos por tipo actualmente en el parqueadero
     */
    public int contarVehiculosActivosPorTipo(String tipo) {
        int contador = 0;
        for (Registro r : obtenerRegistrosActivos()) {
            if (r.getVehiculo().getTipo().equalsIgnoreCase(tipo)) {
                contador++;
            }
        }
        return contador;
    }
    
    /**
     * Calcula el total recaudado en un período
     */
    public double calcularTotalRecaudado() {
        double total = 0;
        for (Registro r : repoRegistro.listarRegistros()) {
            if (r.getFechaSalida() != null && r.getCosto() > 0) {
                total += r.getCosto();
            }
        }
        return total;
    }
    
    /**
     * Obtiene información resumida del parqueadero
     */
    public String obtenerResumenParqueadero() {
        int totalActivos = contarVehiculosActivos();
        int totalAutos = contarVehiculosActivosPorTipo("AUTO");
        int totalMotos = contarVehiculosActivosPorTipo("MOTO");
        double totalRecaudado = calcularTotalRecaudado();
        
        return String.format(
            "📊 RESUMEN DEL PARQUEADERO\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Vehículos activos: %d\n" +
            "  🚗 Autos: %d\n" +
            "  🏍️ Motos: %d\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "💰 Total recaudado: $%.0f",
            totalActivos, totalAutos, totalMotos, totalRecaudado
        );
    }
}