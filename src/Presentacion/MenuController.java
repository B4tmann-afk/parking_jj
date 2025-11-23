package Presentacion;

import Modelos.Usuario;
import Modelos.Rol;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class MenuController {

    private Usuario usuarioLogueado;

    @FXML private Button btnIngreso;
    @FXML private Button btnSalida;
    @FXML private Button btnTarifas;
    @FXML private Button btnVehiculos;
    @FXML private Button btnClientes;
    @FXML private Button btnTrabajadores;

    public void setUsuario(Usuario u) {
        this.usuarioLogueado = u;

        if (u == null) {
            System.out.println("ERROR: usuarioLogueado es NULL");
            return;
        }

        aplicarPermisos();
    }

    private void aplicarPermisos() {
        Rol rol = usuarioLogueado.getRol();

        if (rol == null) {
            System.out.println("ERROR: el rol del usuario es NULL");
            return;
        }

        switch (rol) {

            case ADMIN:
                break;

            case TRABAJADOR:
                btnClientes.setDisable(true);
                btnTrabajadores.setDisable(true);
                break;

            case CLIENTE:
                btnIngreso.setDisable(true);
                btnSalida.setDisable(true);
                btnVehiculos.setDisable(true);
                btnClientes.setDisable(true);
                btnTrabajadores.setDisable(true);
                break;
        }
    }

    // 🚨 MÉTODOS QUE EXIGE EL FXML
  @FXML
private void registrarIngreso() { abrirVentana("vista_ingreso.fxml", "Registrar Ingreso"); }

@FXML
private void registrarSalida() { abrirVentana("vista_salida.fxml", "Registrar Salida"); }

@FXML
private void consultarTarifas() { abrirVentana("vista_tarifas.fxml", "Tarifas"); }

@FXML
private void gestionVehiculos() { abrirVentana("vista_vehiculos.fxml", "Gestión Vehículos"); }

@FXML
private void gestionClientes() { abrirVentana("vista_clientes.fxml", "Gestión Clientes"); }

@FXML
private void gestionTrabajadores() { abrirVentana("vista_trabajadores.fxml", "Gestión Trabajadores"); }

private void abrirVentana(String fxml, String titulo) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

@FXML
private void salir() {
    // Cierra la ventana del menú
    Stage stage = (Stage) btnIngreso.getScene().getWindow();
    stage.close();
}

}