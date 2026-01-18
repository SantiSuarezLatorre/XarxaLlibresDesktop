package com.xarxallibres.desktop.controllers;

import com.xarxallibres.desktop.models.Curso;
import com.xarxallibres.desktop.models.Libro;
import com.xarxallibres.desktop.models.Session;
import com.xarxallibres.desktop.services.CursoService;
import com.xarxallibres.desktop.services.LibroService;
import com.xarxallibres.desktop.utils.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class HomeController {

    // PANEL IZQUIERDO
    @FXML
    private ComboBox<Curso> cursosComboBox;

    @FXML
    private ScrollPane librosScrollPane;

    @FXML
    private VBox librosContainer;

    // PANEL DERECHO
    @FXML
    private Button perfilButton;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField tituloField;

    @FXML
    private TextField editorialField;

    @FXML
    private TextField autorField;

    @FXML
    private TextField asignaturaField;

    @FXML
    private ComboBox<Curso> cursoLibroComboBox;

    @FXML
    private Spinner<Integer> stockTotalSpinner;

    @FXML
    private Spinner<Integer> stockDisponibleSpinner;

    @FXML
    private TextField precioField;

    @FXML
    private TextField imagenUrlField;

    @FXML
    private CheckBox activoCheckBox;

    @FXML
    private Button guardarButton;

    @FXML
    private Button nuevoButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private Label usuarioLabel;

    // Servicios
    private CursoService cursoService;
    private LibroService libroService;

    // Estado
    private Libro libroSeleccionado;
    private ObservableList<Curso> cursos;

    public HomeController() {
        this.cursoService = new CursoService();
        this.libroService = new LibroService();
    }

    @FXML
    public void initialize() {
        configurarSpinners();
        cargarDatosIniciales();
        configurarEventos();
        mostrarInfoUsuario();
    }

    private void configurarSpinners() {
        stockTotalSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0)
        );
        stockDisponibleSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 0)
        );
    }

    private void cargarDatosIniciales() {
        try {
            // Cargar cursos
            List<Curso> listaCursos = cursoService.obtenerCursosActivos();
            cursos = FXCollections.observableArrayList(listaCursos);
            cursosComboBox.setItems(cursos);
            cursoLibroComboBox.setItems(cursos);

            if (!cursos.isEmpty()) {
                cursosComboBox.getSelectionModel().selectFirst();
                cargarLibrosPorCurso();
            }

        } catch (IOException e) {
            AlertHelper.mostrarError("Error", "No se pudieron cargar los cursos: " + e.getMessage());
        }
    }

    private void configurarEventos() {
        // Cambio de curso en filtro
        cursosComboBox.setOnAction(event -> cargarLibrosPorCurso());

        // Botones
        nuevoButton.setOnAction(event -> nuevoLibro());
        guardarButton.setOnAction(event -> guardarLibro());
        eliminarButton.setOnAction(event -> eliminarLibro());
        perfilButton.setOnAction(event -> abrirPerfil());
    }

    private void mostrarInfoUsuario() {
        String nombreUsuario = Session.getInstance().getUsuarioActual().getNombreCompleto();
        usuarioLabel.setText("Bienvenido/a, " + nombreUsuario);
    }

    private void cargarLibrosPorCurso() {
        Curso cursoSeleccionado = cursosComboBox.getValue();
        if (cursoSeleccionado == null) return;

        try {
            List<Libro> libros = libroService.obtenerLibrosPorCurso(cursoSeleccionado.getId());
            mostrarLibros(libros);

        } catch (IOException e) {
            AlertHelper.mostrarError("Error", "No se pudieron cargar los libros: " + e.getMessage());
        }
    }

    private void mostrarLibros(List<Libro> libros) {
        librosContainer.getChildren().clear();

        for (Libro libro : libros) {
            VBox tarjeta = crearTarjetaLibro(libro);
            librosContainer.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjetaLibro(Libro libro) {
        VBox tarjeta = new VBox(8);
        tarjeta.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ddd;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 15;" +
                        "-fx-cursor: hand;"
        );
        tarjeta.setPrefWidth(280);

        // Título
        Label titulo = new Label(libro.getTitulo());
        titulo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        titulo.setWrapText(true);

        // ISBN
        Label isbn = new Label("ISBN: " + libro.getIsbn());
        isbn.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        // Editorial
        Label editorial = new Label("Editorial: " + libro.getEditorial());
        editorial.setStyle("-fx-font-size: 12px;");

        // Asignatura
        Label asignatura = new Label("Asignatura: " + libro.getAsignatura());
        asignatura.setStyle("-fx-font-size: 12px;");

        // Stock
        String stockTexto = String.format("Stock: %d / %d",
                libro.getStockDisponible(), libro.getStockTotal());
        Label stock = new Label(stockTexto);
        stock.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        if (libro.getStockDisponible() == 0) {
            stock.setStyle(stock.getStyle() + "-fx-text-fill: red;");
        }

        tarjeta.getChildren().addAll(titulo, isbn, editorial, asignatura, stock);

        // Evento click
        tarjeta.setOnMouseClicked(event -> seleccionarLibro(libro));

        // Hover effect
        tarjeta.setOnMouseEntered(event ->
                tarjeta.setStyle(tarjeta.getStyle() + "-fx-background-color: #f0f0f0;")
        );
        tarjeta.setOnMouseExited(event ->
                tarjeta.setStyle(tarjeta.getStyle().replace("-fx-background-color: #f0f0f0;",
                        "-fx-background-color: white;"))
        );

        return tarjeta;
    }

    private void seleccionarLibro(Libro libro) {
        this.libroSeleccionado = libro;

        // Cargar datos en formulario
        isbnField.setText(libro.getIsbn());
        tituloField.setText(libro.getTitulo());
        editorialField.setText(libro.getEditorial());
        autorField.setText(libro.getAutor() != null ? libro.getAutor() : "");
        asignaturaField.setText(libro.getAsignatura());

        // Seleccionar curso
        for (Curso c : cursos) {
            if (c.getId().equals(libro.getCursoId())) {
                cursoLibroComboBox.setValue(c);
                break;
            }
        }

        stockTotalSpinner.getValueFactory().setValue(libro.getStockTotal());
        stockDisponibleSpinner.getValueFactory().setValue(libro.getStockDisponible());

        if (libro.getPrecioAproximado() != null) {
            precioField.setText(libro.getPrecioAproximado().toString());
        } else {
            precioField.clear();
        }

        imagenUrlField.setText(libro.getImagenUrl() != null ? libro.getImagenUrl() : "");
        activoCheckBox.setSelected(libro.isActivo());

        // Habilitar botón eliminar
        eliminarButton.setDisable(false);
    }

    @FXML
    private void nuevoLibro() {
        libroSeleccionado = null;
        limpiarFormulario();
        eliminarButton.setDisable(true);
    }

    @FXML
    private void guardarLibro() {
        // Validaciones
        if (tituloField.getText().trim().isEmpty() ||
                isbnField.getText().trim().isEmpty() ||
                editorialField.getText().trim().isEmpty() ||
                asignaturaField.getText().trim().isEmpty() ||
                cursoLibroComboBox.getValue() == null) {

            AlertHelper.mostrarAdvertencia("Campos requeridos",
                    "Por favor, completa todos los campos obligatorios");
            return;
        }

        try {
            Libro libro;

            if (libroSeleccionado == null) {
                // Nuevo libro
                libro = new Libro();
            } else {
                // Actualizar libro existente
                libro = libroSeleccionado;
            }

            // Asignar valores
            libro.setIsbn(isbnField.getText().trim());
            libro.setTitulo(tituloField.getText().trim());
            libro.setEditorial(editorialField.getText().trim());
            libro.setAutor(autorField.getText().trim());
            libro.setAsignatura(asignaturaField.getText().trim());
            libro.setCursoId(cursoLibroComboBox.getValue().getId());
            libro.setStockTotal(stockTotalSpinner.getValue());
            libro.setStockDisponible(stockDisponibleSpinner.getValue());

            if (!precioField.getText().trim().isEmpty()) {
                libro.setPrecioAproximado(new BigDecimal(precioField.getText().trim()));
            }

            libro.setImagenUrl(imagenUrlField.getText().trim());
            libro.setActivo(activoCheckBox.isSelected());

            // Guardar
            if (libroSeleccionado == null) {
                libroService.crearLibro(libro);
                AlertHelper.mostrarExito("Éxito", "Libro creado correctamente");
            } else {
                libroService.actualizarLibro(libro.getId(), libro);
                AlertHelper.mostrarExito("Éxito", "Libro actualizado correctamente");
            }

            // Recargar libros
            cargarLibrosPorCurso();
            limpiarFormulario();

        } catch (NumberFormatException e) {
            AlertHelper.mostrarError("Error", "El precio debe ser un número válido");
        } catch (IOException e) {
            AlertHelper.mostrarError("Error", "No se pudo guardar el libro: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarLibro() {
        if (libroSeleccionado == null) return;

        boolean confirmado = AlertHelper.mostrarConfirmacion(
                "Confirmar eliminación",
                "¿Estás seguro de que quieres desactivar este libro?\n\n" +
                        libroSeleccionado.getTitulo()
        );

        if (confirmado) {
            try {
                libroService.desactivarLibro(libroSeleccionado.getId());
                AlertHelper.mostrarExito("Éxito", "Libro desactivado correctamente");
                cargarLibrosPorCurso();
                limpiarFormulario();

            } catch (IOException e) {
                AlertHelper.mostrarError("Error", "No se pudo eliminar el libro: " + e.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        isbnField.clear();
        tituloField.clear();
        editorialField.clear();
        autorField.clear();
        asignaturaField.clear();
        cursoLibroComboBox.setValue(null);
        stockTotalSpinner.getValueFactory().setValue(0);
        stockDisponibleSpinner.getValueFactory().setValue(0);
        precioField.clear();
        imagenUrlField.clear();
        activoCheckBox.setSelected(true);
    }

    @FXML
    private void abrirPerfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/perfil.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) perfilButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600); // ← Más grande
            stage.setScene(scene);
            stage.setTitle("Mi Perfil");
            stage.setResizable(true); // ← Permitir redimensionar
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.mostrarError("Error", "No se pudo cargar la pantalla de perfil");
        }
    }
}