/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;



import Modelos.ParqueoException.DatosInvalidosException;

public class Validador {
    
    public static String normalizarPlaca(String placa) {
        if (placa == null) return null;
        return placa.trim().toUpperCase().replaceAll("\\s+", "");
    }
    
    public static void validarPlaca(String placa) throws DatosInvalidosException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new DatosInvalidosException("La placa no puede estar vacia");
        }
        
        String placaNormalizada = normalizarPlaca(placa);
        
        if (placaNormalizada.length() < 5 || placaNormalizada.length() > 7) {
            throw new DatosInvalidosException("La placa debe tener entre 5 y 7 caracteres");
        }
        if (!placaNormalizada.matches("[A-Z0-9]+")) {
            throw new DatosInvalidosException("La placa solo puede contener letras mayusculas y numeros");
        }
    }
    
    public static void validarTextoNoVacio(String texto, String campo) throws DatosInvalidosException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatosInvalidosException("El campo " + campo + " no puede estar vacio");
        }
    }
    
    public static void validarCedula(String cedula) throws DatosInvalidosException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new DatosInvalidosException("La cedula no puede estar vacia");
        }
        String cedulaLimpia = cedula.trim().replaceAll("\\s+", "");
        if (!cedulaLimpia.matches("\\d{5,12}")) {
            throw new DatosInvalidosException("La cedula debe contener entre 5 y 12 digitos");
        }
    }
    
    public static void validarTelefono(String telefono) throws DatosInvalidosException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new DatosInvalidosException("El telefono no puede estar vacio");
        }
        String telefonoLimpio = telefono.trim().replaceAll("\\s+", "");
        if (!telefonoLimpio.matches("\\d{7,10}")) {
            throw new DatosInvalidosException("El telefono debe contener entre 7 y 10 digitos");
        }
    }
    
    public static void validarEmail(String email) throws DatosInvalidosException {
        if (email == null || email.trim().isEmpty()) {
            throw new DatosInvalidosException("El email no puede estar vacio");
        }
        String emailLimpio = email.trim();
        if (!emailLimpio.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new DatosInvalidosException("El email no tiene un formato valido");
        }
    }
    
    public static void validarNumeroPositivo(double numero, String campo) throws DatosInvalidosException {
        if (numero <= 0) {
            throw new DatosInvalidosException("El campo " + campo + " debe ser mayor a cero");
        }
    }
    
    public static void validarUsuario(String usuario) throws DatosInvalidosException {
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new DatosInvalidosException("El usuario no puede estar vacio");
        }
        String usuarioLimpio = usuario.trim();
        if (usuarioLimpio.length() < 4) {
            throw new DatosInvalidosException("El usuario debe tener al menos 4 caracteres");
        }
        if (!usuarioLimpio.matches("[A-Za-z0-9_]+")) {
            throw new DatosInvalidosException("El usuario solo puede contener letras, numeros y guion bajo");
        }
    }
    
    public static void validarContraseña(String contraseña) throws DatosInvalidosException {
        if (contraseña == null || contraseña.isEmpty()) {
            throw new DatosInvalidosException("La contraseña no puede estar vacia");
        }
        if (contraseña.length() < 6) {
            throw new DatosInvalidosException("La contraseña debe tener al menos 6 caracteres");
        }
    }
}
