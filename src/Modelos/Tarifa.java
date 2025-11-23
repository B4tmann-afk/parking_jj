/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import Modelos.ParqueoException.DatosInvalidosException;

public class Tarifa {
    private String tipo;
    private double valorHora;
    private double valorDia;

    public Tarifa(String tipo, double valorHora, double valorDia) throws DatosInvalidosException {
        Validador.validarTextoNoVacio(tipo, "tipo");
        Validador.validarNumeroPositivo(valorHora, "valor por hora");
        Validador.validarNumeroPositivo(valorDia, "valor por dia");
        
        this.tipo = tipo.trim().toUpperCase();
        this.valorHora = valorHora;
        this.valorDia = valorDia;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValorHora() {
        return valorHora;
    }

    public double getValorDia() {
        return valorDia;
    }

    public double calcular(long minutos) {
        if (minutos <= 0) return 0;
        
        long horas = (minutos + 59) / 60;
        
        if (horas >= 24) {
            long dias = (horas + 23) / 24;
            return dias * valorDia;
        }
        return horas * valorHora;
    }

    @Override
    public String toString() {
        return "Tarifa{tipo='" + tipo + "', valorHora=$" + valorHora + ", valorDia=$" + valorDia + "}";
    }
}