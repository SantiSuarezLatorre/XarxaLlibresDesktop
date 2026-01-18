package com.xarxallibres.desktop.models;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Libro {
    private String id;
    private String isbn;
    private String titulo;
    private String editorial;
    private String autor;
    private String asignatura;

    @SerializedName("curso_id")
    private String cursoId;

    @SerializedName("stock_total")
    private int stockTotal;

    @SerializedName("stock_disponible")
    private int stockDisponible;

    @SerializedName("precio_aproximado")
    private BigDecimal precioAproximado;

    @SerializedName("imagen_url")
    private String imagenUrl;

    private boolean activo;

    @SerializedName("created_at")
    private LocalDateTime createdAt;

    @SerializedName("updated_at")
    private LocalDateTime updatedAt;

    // Para mostrar el nombre del curso (JOIN)
    @SerializedName("curso_nombre")
    private String cursoNombre;

    // Constructor vacío
    public Libro() {
        this.activo = true;
        this.stockTotal = 0;
        this.stockDisponible = 0;
    }

    // Constructor básico
    public Libro(String isbn, String titulo, String editorial, String asignatura, String cursoId) {
        this();
        this.isbn = isbn;
        this.titulo = titulo;
        this.editorial = editorial;
        this.asignatura = asignatura;
        this.cursoId = cursoId;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getAsignatura() { return asignatura; }
    public void setAsignatura(String asignatura) { this.asignatura = asignatura; }

    public String getCursoId() { return cursoId; }
    public void setCursoId(String cursoId) { this.cursoId = cursoId; }

    public int getStockTotal() { return stockTotal; }
    public void setStockTotal(int stockTotal) { this.stockTotal = stockTotal; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }

    public BigDecimal getPrecioAproximado() { return precioAproximado; }
    public void setPrecioAproximado(BigDecimal precioAproximado) {
        this.precioAproximado = precioAproximado;
    }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCursoNombre() { return cursoNombre; }
    public void setCursoNombre(String cursoNombre) { this.cursoNombre = cursoNombre; }

    public boolean isDisponible() {
        return activo && stockDisponible > 0;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", stock=" + stockDisponible + "/" + stockTotal +
                '}';
    }
}