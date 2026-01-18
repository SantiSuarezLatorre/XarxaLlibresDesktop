package com.xarxallibres.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar la vista de login
        Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));

        Scene scene = new Scene(root, 400, 500);

        primaryStage.setTitle("XarxaLlibres - Inicio de Sesión");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}