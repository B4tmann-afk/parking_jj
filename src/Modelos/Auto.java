/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;


import Modelos.ParqueoException.DatosInvalidosException;

public class Auto extends Vehiculo {
    public Auto(String placa, String marca, String modelo) throws DatosInvalidosException {
        super(placa, marca, modelo, "AUTO");
    }
}

