package com.xarxallibres.desktop.controllers;

import com.xarxallibres.desktop.services.AuthService;
import com.xarxallibres.desktop.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    public void initialize() {
        // Configurar evento del botón
        loginButton.setOnAction(event -> handleLogin());

        // Permitir login con Enter
        passwordField.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Validaciones
        if (email.isEmpty() || password.isEmpty()) {
            AlertHelper.mostrarAdvertencia("Campos vacíos",
                    "Por favor, introduce tu email y contraseña");
            return;
        }

        // Deshabilitar botón mientras se procesa
        loginButton.setDisable(true);
        loginButton.setText("Iniciando sesión...");

        try {
            // Intentar login
            boolean exito = authService.login(email, password);

            if (exito) {
                // Login exitoso - ir a pantalla principal
                abrirPantallaHome();
            }

        } catch (IOException e) {
            AlertHelper.mostrarError("Error de inicio de sesión", e.getMessage());
        } finally {
            // Rehabilitar botón
            loginButton.setDisable(false);
            loginButton.setText("Iniciar Sesión");
        }
    }

    private void abrirPantallaHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            stage.setScene(scene);
            stage.setTitle("XarxaLlibres - Administración");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.mostrarError("Error", "No se pudo cargar la pantalla principal");
        }
    }
}