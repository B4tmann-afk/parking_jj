/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

public class ParqueoException extends Exception {
    public ParqueoException(String mensaje) {
        super(mensaje);
    }
    
    public static class VehiculoNoEncontradoException extends ParqueoException {
        public VehiculoNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class EspacioNoDisponibleException extends ParqueoException {
        public EspacioNoDisponibleException(String mensaje) {
            super(mensaje);
        }
    }

    public static class EspaciosLlenosException extends ParqueoException {
        public EspaciosLlenosException(String mensaje) {
            super(mensaje);
        }
    }

    public static class PlacaDuplicadaException extends ParqueoException {
        public PlacaDuplicadaException(String mensaje) {
            super(mensaje);
        }
    }

    public static class TarifaNoDefinidaException extends ParqueoException {
        public TarifaNoDefinidaException(String mensaje) {
            super(mensaje);
        }
    }

    public static class UsuarioNoEncontradoException extends ParqueoException {
        public UsuarioNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }

    public static class CredencialesInvalidasException extends ParqueoException {
        public CredencialesInvalidasException(String mensaje) {
            super(mensaje);
        }
    }

    public static class DatosInvalidosException extends ParqueoException {
        public DatosInvalidosException(String mensaje) {
            super(mensaje);
        }
    }
    
    public static class ClienteNoEncontradoException extends ParqueoException {
        public ClienteNoEncontradoException(String mensaje) {
            super(mensaje);
        }
    }
}