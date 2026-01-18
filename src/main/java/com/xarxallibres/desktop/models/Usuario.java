package com.xarxallibres.desktop.models;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;

public class Usuario {
    private String id;
    private String email;

    @SerializedName("password_hash")
    private String passwordHash;

    private String nombre;
    private String apellidos;
    private String telefono;

    @SerializedName("tipo_usuario")
    private String tipoUsuario;

    @SerializedName("curso_id")
    private String cursoId;

    private boolean activo;

    @SerializedName("puede_recoger")
    private boolean puedeRecoger;

    @SerializedName("es_primer_anio")
    private boolean esPrimerAnio;

    @SerializedName("auth_uid")
    private String authUid;

    @SerializedName("created_at")
    private LocalDateTime createdAt;

    @SerializedName("updated_at")
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Usuario() {}

    // Constructor completo
    public Usuario(String id, String email, String nombre, String apellidos,
                   String tipoUsuario) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.tipoUsuario = tipoUsuario;
        this.activo = true;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getCursoId() { return cursoId; }
    public void setCursoId(String cursoId) { this.cursoId = cursoId; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public boolean isPuedeRecoger() { return puedeRecoger; }
    public void setPuedeRecoger(boolean puedeRecoger) { this.puedeRecoger = puedeRecoger; }

    public boolean isEsPrimerAnio() { return esPrimerAnio; }
    public void setEsPrimerAnio(boolean esPrimerAnio) { this.esPrimerAnio = esPrimerAnio; }

    public String getAuthUid() { return authUid; }
    public void setAuthUid(String authUid) { this.authUid = authUid; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}