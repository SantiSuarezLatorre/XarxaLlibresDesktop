package com.xarxallibres.desktop.models;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class Curso {
    private String id;
    private String nombre;
    private String nivel;
    private String grupo;

    @SerializedName("curso_academico")
    private String cursoAcademico;

    private boolean activo;

    @SerializedName("created_at")
    private LocalDateTime createdAt;

    @SerializedName("updated_at")
    private LocalDateTime updatedAt;

    public Curso() {
        this.activo = true;
        this.cursoAcademico = "2025/2026";
    }

    public Curso(String nombre, String nivel) {
        this();
        this.nombre = nombre;
        this.nivel = nivel;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }

    public String getCursoAcademico() { return cursoAcademico; }
    public void setCursoAcademico(String cursoAcademico) {
        this.cursoAcademico = cursoAcademico;
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return nombre;
    }
}