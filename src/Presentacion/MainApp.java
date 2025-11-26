package Presentacion;

import Logica.DatosIniciales;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Cargar usuarios antes de abrir el login
        DatosIniciales.cargarUsuarios();
        
        // RUTA CORREGIDA: /Views/login.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/login.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setTitle("Login - Parking JJ");
        stage.setScene(scene);
        stage.setResizable(false); // Opcional: evitar que se redimensione
        stage.show();
    }
    
    public static void main(String[] args) {
        launch();
    }
}