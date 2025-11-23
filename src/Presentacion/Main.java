/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;


import Modelos.*;
import Persistencia.*;
import Logica.*;
import java.util.*;

public class Main {
    private final RepositorioVehiculo repoVehiculo;
    private final RepositorioRegistro repoRegistro;
    private final RepositorioTarifa repoTarifa;
    private final RepositorioUsuario repoUsuario;
    private final RepositorioCliente repoCliente;

    private final ServicioParqueo servicioParqueo;
    private final ServicioVehiculo servicioVehiculo;
    private final ServicioTarifa servicioTarifa;
    private final ServicioAutenticacion servicioAutenticacion;
    private final ServicioAdministrador servicioAdministrador;
    private final ServicioCliente servicioCliente;

    private final Scanner scanner;
    private Usuario usuarioActual;

    public Main() {
        this.repoVehiculo = new RepositorioVehiculo();
        this.repoRegistro = new RepositorioRegistro();
        this.repoTarifa = new RepositorioTarifa();
        this.repoUsuario = new RepositorioUsuario();
        this.repoCliente = new RepositorioCliente();

        this.servicioParqueo = new ServicioParqueo(repoVehiculo, repoRegistro, repoTarifa);
        this.servicioVehiculo = new ServicioVehiculo(repoVehiculo);
        this.servicioTarifa = new ServicioTarifa(repoTarifa);
        this.servicioAutenticacion = new ServicioAutenticacion(repoUsuario);
        this.servicioAdministrador = new ServicioAdministrador(repoUsuario, repoRegistro);
        this.servicioCliente = new ServicioCliente(repoCliente);

        this.scanner = new Scanner(System.in);
        this.usuarioActual = null;

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        try {
            Administrador admin = new Administrador("1", "Jainer Cuello", "1067598680", "3001234567", "admin", "rikiti123");
            Trabajador trabajador = new Trabajador("2", "Jose Hernandez", "9876543210", "3007654321", "trabajador", "trabajo123", "Mañana", 1500000);
            Cliente cliente = new Cliente("3", "Alfredo Bautista", "1111111111", "3111111111", "pedro@email.com", "Calle 1 #2-3");

            repoUsuario.guardar(admin);
            repoUsuario.guardar(trabajador);
            repoCliente.guardar(cliente);

            servicioTarifa.crearTarifa("AUTO", 5000, 80000);
            servicioTarifa.crearTarifa("MOTO", 3000, 50000);
            
            System.out.println("\n✓ Sistema inicializado correctamente");
            System.out.println("  - Usuarios de prueba creados");
            System.out.println("  - Tarifas configuradas\n");
        } catch (Exception e) {
            System.out.println(" Error al cargar datos iniciales: " + e.getMessage());
        }
    }

    public void iniciar() {
        System.out.println("+========================================+");
        System.out.println("|      BIENVENIDO A PARKING JJ           |");
        System.out.println("|  Sistema de Gestion de Parqueadero     |");
        System.out.println("+========================================+\n");

        boolean ejecutando = true;
        while (ejecutando) {
            try {
                if (usuarioActual == null) {
                    menuLogin();
                } else if (usuarioActual instanceof Administrador) {
                    menuAdministrador();
                } else if (usuarioActual instanceof Trabajador) {
                    menuTrabajador();
                }
            } catch (Exception e) {
                System.out.println(" Error inesperado: " + e.getMessage());
            }
        }
    }

    private void menuLogin() {
        System.out.println("\n+=== MENU DE LOGIN ===+");
        System.out.println("| 1. Iniciar sesion   |");
        System.out.println("| 0. Salir            |");
        System.out.println("+=====================+");
        System.out.print("Seleccione una opcion: ");

        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                login();
                break;
            case "0":
                System.out.println("\n¡Gracias por usar PARKING JJ!");
                System.out.println("¡Hasta luego!\n");
                System.exit(0);
                break;
            default:
                System.out.println(" Opción invalida");
        }
    }

    private void login() {
        System.out.print("\nUsuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = scanner.nextLine();
        usuarioActual = servicioAutenticacion.login(usuario, contraseña);
        System.out.println("\n✓ ¡Bienvenido " + usuarioActual.getNombre() + "!");
    }

    private void menuAdministrador() {
        System.out.println("\n+=== MENU ADMINISTRADOR ===+");
        System.out.println("| 1. Ver tarifas            |");
        System.out.println("| 2. Actualizar tarifa      |");
        System.out.println("| 3. Crear usuario          |");
        System.out.println("| 4. Generar reporte        |");
        System.out.println("| 5. Listar usuarios        |");
        System.out.println("| 6. Ver vehiculos activos  |");
        System.out.println("| 7. Listar todos vehiculos |");
        System.out.println("| 0. Cerrar sesion          |");
        System.out.println("+===========================+");
        System.out.print("Seleccione una opcion: ");

        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                verTarifas();
                break;
            case "2":
                actualizarTarifa();
                break;
            case "3":
                crearUsuario();
                break;
            case "4":
                servicioAdministrador.generarReporte();
                break;
            case "5":
                listarUsuarios();
                break;
            case "6":
                verVehiculosActivos();
                break;
            case "7":
                listarTodosVehiculos();
                break;
            case "0":
                usuarioActual = null;
                System.out.println(" Sesion cerrada exitosamente");
                break;
            default:
                System.out.println(" Opcion invalida");
        }
    }

    private void menuTrabajador() {
        System.out.println("\n+=== MENU TRABAJADOR ===+");
        System.out.println("| 1. Ingreso de vehiculo |");
        System.out.println("| 2. Salida de vehiculo  |");
        System.out.println("| 3. Ver vehiculos       |");
        System.out.println("| 4. Ver tarifas         |");
        System.out.println("| 0. Cerrar sesion       |");
        System.out.println("+========================+");
        System.out.print("Seleccione una opción: ");

        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                registrarIngreso();
                break;
            case "2":
                registrarSalida();
                break;
            case "3":
                verVehiculosActivos();
                break;
            case "4":
                verTarifas();
                break;
            case "0":
                usuarioActual = null;
                System.out.println(" Sesion cerrada exitosamente");
                break;
            default:
                System.out.println(" Opcion invalida");
        }
    }

    private void registrarIngreso() {
        try {
            System.out.println("\n--- REGISTRO DE INGRESO ---");
            System.out.print("Tipo de vehiculo (AUTO/MOTO): ");
            String tipo = scanner.nextLine();
            
            if (tipo == null || tipo.trim().isEmpty()) {
                System.out.println(" El tipo no puede estar vacio");
                return;
            }
            
            String tipoNormalizado = tipo.trim().toUpperCase();
            if (!tipoNormalizado.equals("AUTO") && !tipoNormalizado.equals("MOTO")) {
                System.out.println(" Tipo invalido. Debe ser AUTO o MOTO");
                return;
            }
            
            System.out.print("Placa: ");
            String placa = scanner.nextLine();
            System.out.print("Marca: ");
            String marca = scanner.nextLine();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine();

            Vehiculo vehiculo;
            if (tipoNormalizado.equals("MOTO")) {
                vehiculo = new Moto(placa, marca, modelo);
            } else {
                vehiculo = new Auto(placa, marca, modelo);
            }
            
            servicioParqueo.ingresarVehiculo(vehiculo);
        } catch (ParqueoException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void registrarSalida() {
        try {
            System.out.println("\n--- REGISTRO DE SALIDA ---");
            System.out.print("Placa del vehiculo: ");
            String placa = scanner.nextLine();

            if (placa == null || placa.trim().isEmpty()) {
                System.out.println(" La placa no puede estar vacia");
                return;
            }

            double costo = servicioParqueo.salidaVehiculo(placa);
            System.out.println("\n+===========================+");
            System.out.println("|      TICKET DE PAGO       |");
            System.out.println("|===========================|");
            System.out.println("| Total a pagar: $" + String.format("%-10.2f", costo) + "║");
            System.out.println("+===========================+\n");
        } catch (ParqueoException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void verTarifas() {
        List<Tarifa> tarifas = servicioTarifa.listarTarifas();
        
        System.out.println("\n+======================================+");
        System.out.println("|         TARIFAS ACTUALES              |");
        System.out.println("+=======================================+");
        
        if (tarifas.isEmpty()) {
            System.out.println("No hay tarifas configuradas\n");
            return;
        }
        
        for (Tarifa t : tarifas) {
            System.out.println("\n" + t.getTipo() + ":");
            System.out.println("  • Por hora: $" + String.format("%.2f", t.getValorHora()));
            System.out.println("  • Por día:  $" + String.format("%.2f", t.getValorDia()));
        }
        System.out.println();
    }

    private void actualizarTarifa() {
        try {
            System.out.println("\n--- ACTUALIZAR TARIFA ---");
            System.out.print("Tipo (AUTO/MOTO): ");
            String tipo = scanner.nextLine();
            
            if (tipo == null || tipo.trim().isEmpty()) {
                System.out.println(" El tipo no puede estar vacio");
                return;
            }
            
            String tipoNormalizado = tipo.trim().toUpperCase();
            if (!tipoNormalizado.equals("AUTO") && !tipoNormalizado.equals("MOTO")) {
                System.out.println(" Tipo invalido. Debe ser AUTO o MOTO");
                return;
            }
            
            System.out.print("Nueva tarifa por hora: $");
            String horaStr = scanner.nextLine();
            System.out.print("Nueva tarifa por día: $");
            String diaStr = scanner.nextLine();

            if (horaStr == null || horaStr.trim().isEmpty() || diaStr == null || diaStr.trim().isEmpty()) {
                System.out.println(" Los valores no pueden estar vacios");
                return;
            }

            try {
                double hora = Double.parseDouble(horaStr.trim());
                double dia = Double.parseDouble(diaStr.trim());
                
                if (hora <= 0 || dia <= 0) {
                    System.out.println(" Los valores deben ser mayores a cero");
                    return;
                }
                
                servicioTarifa.actualizarTarifa(tipoNormalizado, hora, dia);
            } catch (NumberFormatException e) {
                System.out.println(" Valores numericos invalidos");
            }
        } catch (ParqueoException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void crearUsuario() {
        try {
            System.out.println("\n--- CREAR NUEVO USUARIO ---");
            System.out.print("ID: ");
            String id = scanner.nextLine();
            System.out.print("Nombre completo: ");
            String nombre = scanner.nextLine();
            System.out.print("Cedula: ");
            String cedula = scanner.nextLine();
            System.out.print("Telefono: ");
            String telefono = scanner.nextLine();
            System.out.print("Usuario (login): ");
            String usuario = scanner.nextLine();
            System.out.print("Contraseña: ");
            String contraseña = scanner.nextLine();
            System.out.print("Tipo (ADMINISTRADOR/TRABAJADOR): ");
            String tipo = scanner.nextLine();

            if (tipo == null || tipo.trim().isEmpty()) {
                System.out.println(" El tipo no puede estar vacio");
                return;
            }

            String tipoNormalizado = tipo.trim().toUpperCase();
            Usuario nuevoUsuario;
            
            if (tipoNormalizado.equals("TRABAJADOR")) {
                System.out.print("Turno (Mañana/Tarde/Noche): ");
                String turno = scanner.nextLine();
                System.out.print("Salario: $");
                String salarioStr = scanner.nextLine();
                
                if (salarioStr == null || salarioStr.trim().isEmpty()) {
                    System.out.println(" El salario no puede estar vacio");
                    return;
                }
                
                try {
                    double salario = Double.parseDouble(salarioStr.trim());
                    nuevoUsuario = new Trabajador(id, nombre, cedula, telefono, usuario, contraseña, turno, salario);
                } catch (NumberFormatException e) {
                    System.out.println(" Salario invalido");
                    return;
                }
            } else if (tipoNormalizado.equals("ADMINISTRADOR")) {
                nuevoUsuario = new Administrador(id, nombre, cedula, telefono, usuario, contraseña);
            } else {
                System.out.println(" Tipo invalido. Debe ser ADMINISTRADOR o TRABAJADOR");
                return;
            }
            
            servicioAdministrador.crearUsuario(nuevoUsuario);
        } catch (ParqueoException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = servicioAdministrador.listarUsuarios();
        
        System.out.println("\n+======================================+");
        System.out.println("|       USUARIOS REGISTRADOS            |");
        System.out.println("+======================================+");
        
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados\n");
            return;
        }
        
        int adminCount = 0;
        int trabajadorCount = 0;
        
        for (Usuario u : usuarios) {
            System.out.println("\n" + u);
            if (u instanceof Administrador) {
                adminCount++;
            } else if (u instanceof Trabajador) {
                trabajadorCount++;
            }
        }
        
        System.out.println("\n" + "─".repeat(40));
        System.out.println("Total: " + usuarios.size() + " usuarios");
        System.out.println("  • Administradores: " + adminCount);
        System.out.println("  • Trabajadores: " + trabajadorCount);
        System.out.println();
    }

    private void verVehiculosActivos() {
        List<Registro> registros = servicioParqueo.obtenerRegistrosActivos();
        
        System.out.println("\n+======================================+");
        System.out.println("|      VEHICULOS EN PARQUEO             |");
        System.out.println("+======================================+\n");
        
        if (registros.isEmpty()) {
            System.out.println("No hay vehIculos en el parqueo actualmente\n");
            return;
        }
        
        for (Registro r : registros) {
            long minutos = r.calcularMinutos();
            System.out.println("• Placa: " + r.getVehiculo().getPlaca() + 
                               " | Tipo: " + r.getVehiculo().getTipo() +
                               " | Tiempo: " + minutos + " min" +
                               " | Registro #" + r.getId());
        }
        
        System.out.println("\nTotal de vehiculos: " + registros.size());
        System.out.println();
    }

    private void listarTodosVehiculos() {
        List<Vehiculo> vehiculos = servicioVehiculo.listarVehiculos();
        
        System.out.println("\n+======================================+");
        System.out.println("|       VEHICULOS REGISTRADOS           |");
        System.out.println("+======================================+\n");
        
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehiculos registrados\n");
            return;
        }
        
        for (Vehiculo v : vehiculos) {
            System.out.println("• " + v);
        }
        
        System.out.println("\nTotal: " + vehiculos.size() + " vehiculos");
        System.out.println();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.iniciar();
    }
}