package Controller;

import Controller.MenuController;
import Persistencia.RepositorioUsuario;
import Modelos.Usuario;
import Modelos.ParqueoException.UsuarioNoEncontradoException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    
    @FXML
    private TextField txtUsuario;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private void iniciarSesion() {
        try {
            String user = txtUsuario.getText();
            String pass = txtPassword.getText();
            
            Usuario u = RepositorioUsuario.buscar(user);
            
            if (!u.validarCredenciales(user, pass)) {
                mostrar("Error", "Contraseña incorrecta");
                return;
            }
            
            abrirMenu(u);
            
        } catch (Exception e) {
            mostrar("Error", e.getMessage());
        }
    }
    
    private void abrirMenu(Usuario u) {
        try {
            // RUTA CORREGIDA: /Views/vista_menu.fxml
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/Views/vista_menu.fxml")
            );
            Parent root = loader.load();
            
            MenuController controller = loader.getController();
            controller.setUsuario(u);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menú Principal - Parking JJ");
            stage.show();
            
            // Cerrar ventana de login
            ((Stage) txtUsuario.getScene().getWindow()).close();
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrar("Error", "No se pudo abrir el menú: " + e.getMessage());
        }
    }
    
    private void mostrar(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}