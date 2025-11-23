/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Registro {
    private static final AtomicInteger contador = new AtomicInteger(1);
    private int id;
    private Vehiculo vehiculo;
    
    private LocalDateTime fechaEntrada;
    private LocalDateTime fechaSalida;
    private double costo;

    public Registro(Vehiculo vehiculo ) {
        this.id = contador.getAndIncrement();
        this.vehiculo = vehiculo;
       
        this.fechaEntrada = LocalDateTime.now();
        this.fechaSalida = null;
        this.costo = 0;
    }

    public int getId() {
        return id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public long calcularMinutos() {
        if (fechaSalida == null) {
            return ChronoUnit.MINUTES.between(fechaEntrada, LocalDateTime.now());
        }
        return ChronoUnit.MINUTES.between(fechaEntrada, fechaSalida);
    }

    @Override
    public String toString() {
        return "Registro{id=" + id + ", vehiculo=" + vehiculo.getPlaca() + 
                ", entrada=" + fechaEntrada + 
               ", salida=" + fechaSalida + ", costo=$" + costo + "}";
    }
}