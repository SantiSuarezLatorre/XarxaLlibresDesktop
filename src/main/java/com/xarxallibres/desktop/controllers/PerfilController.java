package com.xarxallibres.desktop.controllers;

import com.xarxallibres.desktop.models.Session;
import com.xarxallibres.desktop.models.Usuario;
import com.xarxallibres.desktop.services.AuthService;
import com.xarxallibres.desktop.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class PerfilController {

    @FXML
    private Label nombreLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label tipoUsuarioLabel;

    @FXML
    private Label telefonoLabel;

    @FXML
    private Button volverButton;

    @FXML
    private Button cerrarSesionButton;

    private AuthService authService;

    public PerfilController() {
        this.authService = new AuthService();
    }

    @FXML
    public void initialize() {
        cargarDatosUsuario();
        configurarEventos();
    }

    private void cargarDatosUsuario() {
        Usuario usuario = Session.getInstance().getUsuarioActual();

        if (usuario != null) {
            nombreLabel.setText(usuario.getNombreCompleto());
            emailLabel.setText(usuario.getEmail());
            tipoUsuarioLabel.setText(
                    usuario.getTipoUsuario().substring(0, 1).toUpperCase() +
                            usuario.getTipoUsuario().substring(1)
            );

            if (usuario.getTelefono() != null && !usuario.getTelefono().isEmpty()) {
                telefonoLabel.setText(usuario.getTelefono());
            } else {
                telefonoLabel.setText("No especificado");
            }
        }
    }

    private void configurarEventos() {
        volverButton.setOnAction(event -> volverHome());
        cerrarSesionButton.setOnAction(event -> cerrarSesion());
    }

    @FXML
    private void volverHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) volverButton.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            stage.setScene(scene);
            stage.setTitle("XarxaLlibres - Administración");
            stage.centerOnScreen(); // Centrar la ventana
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.mostrarError("Error", "No se pudo cargar la pantalla principal: " + e.getMessage());
        }
    }

    @FXML
    private void cerrarSesion() {
        boolean confirmado = AlertHelper.mostrarConfirmacion(
                "Cerrar Sesión",
                "¿Estás seguro de que quieres cerrar sesión?"
        );

        if (confirmado) {
            authService.logout();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) cerrarSesionButton.getScene().getWindow();
                Scene scene = new Scene(root, 400, 500);
                stage.setScene(scene);
                stage.setTitle("XarxaLlibres - Inicio de Sesión");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                AlertHelper.mostrarError("Error", "No se pudo cargar la pantalla de login");
            }
        }
    }
}