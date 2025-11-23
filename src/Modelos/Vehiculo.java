
package Modelos;


import Modelos.ParqueoException.DatosInvalidosException;

public abstract class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private String tipo;

    public Vehiculo(String placa, String marca, String modelo, String tipo) throws DatosInvalidosException {
        if (placa == null || marca == null || modelo == null) {
            throw new DatosInvalidosException("Los datos del vehiculo no pueden ser nulos");
        }
        
        String placaNormalizada = Validador.normalizarPlaca(placa);
        Validador.validarPlaca(placaNormalizada);
        Validador.validarTextoNoVacio(marca, "marca");
        Validador.validarTextoNoVacio(modelo, "modelo");
        
        this.placa = placaNormalizada;
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tipo = tipo.toUpperCase();
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "Vehiculo{placa='" + placa + "', marca='" + marca + "', modelo='" + modelo + "', tipo='" + tipo + "'}";
    }
}