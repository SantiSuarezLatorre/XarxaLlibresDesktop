package com.xarxallibres.desktop.models;

public class Session {
    private static Session instance;
    private Usuario usuarioActual;
    private String accessToken;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public boolean isLoggedIn() {
        return usuarioActual != null && accessToken != null;
    }

    public void logout() {
        usuarioActual = null;
        accessToken = null;
    }
}