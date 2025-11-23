/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import Modelos.*;
import Modelos.ParqueoException;
import Modelos.ParqueoException.DatosInvalidosException;
import Modelos.ParqueoException.TarifaNoDefinidaException;
import Persistencia.RepositorioRegistro;
import Persistencia.RepositorioUsuario;
import Persistencia.RepositorioTarifa;
import Persistencia.RepositorioVehiculo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para operaciones de administrador.
 */
public class ServicioAdministrador {
    private final RepositorioUsuario repoUsuario;
    private final ServicioTarifa servicioTarifa;
    private final ServicioParqueo servicioParqueo;

    /**
     * Constructor principal usado cuando ya tienes los servicios creados.
     */
    public ServicioAdministrador(RepositorioUsuario repoUsuario, ServicioTarifa servicioTarifa, ServicioParqueo servicioParqueo) {
        if (repoUsuario == null || servicioTarifa == null || servicioParqueo == null) {
            throw new IllegalArgumentException("Los servicios no pueden ser nulos");
        }
        this.repoUsuario = repoUsuario;
        this.servicioTarifa = servicioTarifa;
        this.servicioParqueo = servicioParqueo;
    }

    /**
     * Constructor utilizado por tu Main: recibe Repositorios y construye los servicios internos.
     * Este constructor evita lanzar UnsupportedOperationException y deja el servicio listo.
     */
    public ServicioAdministrador(RepositorioUsuario repoUsuario, RepositorioRegistro repoRegistro) {
        if (repoUsuario == null || repoRegistro == null) {
            throw new IllegalArgumentException("RepositorioUsuario y RepositorioRegistro no pueden ser nulos");
        }
        this.repoUsuario = repoUsuario;

        // Crear repositorios auxiliares y servicios usados por el administrador
        RepositorioTarifa repoTarifa = new RepositorioTarifa();
        RepositorioVehiculo repoVehiculo = new RepositorioVehiculo();

        this.servicioTarifa = new ServicioTarifa(repoTarifa);
        this.servicioParqueo = new ServicioParqueo(repoVehiculo, repoRegistro, repoTarifa);
    }

    public List<Usuario> listarTrabajadores() {
        return repoUsuario.listarTodos().stream()
                .filter(u -> u instanceof Trabajador)
                .collect(Collectors.toList());
    }

    public List<Usuario> listarAdministradores() {
        return repoUsuario.listarTodos().stream()
                .filter(u -> u instanceof Administrador)
                .collect(Collectors.toList());
    }

    public List<Usuario> listarUsuarios() {
        return repoUsuario.listarTodos();
    }

    public void configurarTarifas(double valorHoraAuto, double valorDiaAuto, double valorHoraMoto, double valorDiaMoto) throws DatosInvalidosException, TarifaNoDefinidaException {
        try {
            servicioTarifa.crearTarifa("AUTO", valorHoraAuto, valorDiaAuto);
        } catch (DatosInvalidosException e) {
            try {
                servicioTarifa.actualizarTarifa("AUTO", valorHoraAuto, valorDiaAuto);
            } catch (TarifaNoDefinidaException ex) {
                throw new DatosInvalidosException("Error al configurar tarifa AUTO: " + ex.getMessage());
            }
        }

        try {
            servicioTarifa.crearTarifa("MOTO", valorHoraMoto, valorDiaMoto);
        } catch (DatosInvalidosException e) {
            try {
                servicioTarifa.actualizarTarifa("MOTO", valorHoraMoto, valorDiaMoto);
            } catch (TarifaNoDefinidaException ex) {
                throw new DatosInvalidosException("Error al configurar tarifa MOTO: " + ex.getMessage());
            }
        }
    }

    public double calcularIngresosDelDia() {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        return servicioParqueo.obtenerRegistros().stream()
                .filter(r -> r.getFechaSalida() != null)
                .filter(r -> r.getFechaSalida().isAfter(inicioDia))
                .mapToDouble(Registro::getCosto)
                .sum();
    }

    public double calcularIngresosTotales() {
        return servicioParqueo.obtenerRegistros().stream()
                .filter(r -> r.getFechaSalida() != null)
                .mapToDouble(Registro::getCosto)
                .sum();
    }

    public int contarVehiculosActuales() {
        return (int) servicioParqueo.obtenerRegistros().stream()
                .filter(r -> r.getFechaSalida() == null)
                .count();
    }

    public List<Registro> obtenerHistorialCompleto() {
        return servicioParqueo.obtenerRegistros();
    }

    public List<Registro> obtenerRegistrosActivos() {
        return servicioParqueo.obtenerRegistros().stream()
                .filter(r -> r.getFechaSalida() == null)
                .collect(Collectors.toList());
    }

    public List<Tarifa> obtenerTarifasActuales() {
        return servicioTarifa.listarTarifas();
    }

    public void generarReporte() {
        List<Registro> registros = servicioParqueo.obtenerRegistros();
        double totalIngresos = calcularIngresosTotales();
        double ingresosHoy = calcularIngresosDelDia();
        int totalVehiculos = registros.size();
        int vehiculosActivos = contarVehiculosActuales();

        System.out.println("\n+================================================================+");
        System.out.println("|              REPORTE DE PARQUEO - PARKING JJ                   |");
        System.out.println("+================================================================+\n");

        for (Registro r : registros) {
            if (r.getFechaSalida() == null) {
                System.out.println("[ACTIVO] " + r);
            } else {
                System.out.println("[FINALIZADO] " + r);
            }
        }

        System.out.println("\n" + "=".repeat(64));
        System.out.println("Total de registros: " + totalVehiculos);
        System.out.println("Vehiculos actualmente en parqueo: " + vehiculosActivos);
        System.out.println("Ingresos del dia: $" + String.format("%.2f", ingresosHoy));
        System.out.println("Total de ingresos generados: $" + String.format("%.2f", totalIngresos));
        System.out.println("=".repeat(64) + "\n");
    }

    /**
     * Crea un usuario validando duplicados por ID o login.
     * Lanza ParqueoException si hay conflicto o error.
     */
    public void crearUsuario(Usuario nuevoUsuario) throws ParqueoException {
        if (nuevoUsuario == null) {
            throw new ParqueoException("Usuario invalido (nulo)");
        }

        // Validaciones básicas: id y login únicos
        for (Usuario u : repoUsuario.listarTodos()) {
            if (u.getId() != null && u.getId().equalsIgnoreCase(nuevoUsuario.getId())) {
                throw new ParqueoException("Ya existe un usuario con el ID: " + nuevoUsuario.getId());
            }
            // asumimos que Usuario tiene método getUsuario() que devuelve el login
            if (u.getUsuario() != null && u.getUsuario().equalsIgnoreCase(nuevoUsuario.getUsuario())) {
                throw new ParqueoException("El nombre de usuario (login) ya esta en uso: " + nuevoUsuario.getUsuario());
            }
        }

        // Guardar usuario en repositorio
        repoUsuario.guardar(nuevoUsuario);
        System.out.println(" Usuario creado correctamente: " + nuevoUsuario.getNombre());
    }
}