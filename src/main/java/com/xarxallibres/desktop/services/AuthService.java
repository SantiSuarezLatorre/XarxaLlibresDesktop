package com.xarxallibres.desktop.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xarxallibres.desktop.config.SupabaseConfig;
import com.xarxallibres.desktop.models.Session;
import com.xarxallibres.desktop.models.Usuario;
import okhttp3.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class AuthService extends SupabaseService {

    /**
     * Login de usuario (profesores/admin)
     */
    public boolean login(String email, String password) throws IOException {
        // 1. Buscar usuario por email
        Usuario usuario = buscarUsuarioPorEmail(email);

        System.out.println("Usuario encontrado: " + usuario);

        if (usuario == null) {
            throw new IOException("Usuario no encontrado");
        }

        // 2. Verificar que es profesor o admin
        if (!usuario.getTipoUsuario().equals("profesor") &&
                !usuario.getTipoUsuario().equals("admin")) {
            throw new IOException("Acceso denegado. Solo profesores y administradores");
        }

        // 3. Verificar contraseña

        try {
            boolean matches = BCrypt.checkpw(password, usuario.getPasswordHash());
            System.out.println("BCrypt.checkpw resultado: " + matches);

            if (!matches) {
                throw new IOException("Contraseña incorrecta");
            }
        } catch (Exception e) {
            System.out.println("Error en BCrypt: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error al verificar contraseña: " + e.getMessage());
        }

        // 4. Verificar que está activo
        if (!usuario.isActivo()) {
            throw new IOException("Usuario inactivo");
        }

        // 5. Guardar sesión
        Session session = Session.getInstance();
        session.setUsuarioActual(usuario);
        session.setAccessToken(SupabaseConfig.SUPABASE_KEY);

        System.out.println("=== LOGIN EXITOSO ===");
        return true;
    }

    /**
     * Buscar usuario por email
     */
    private Usuario buscarUsuarioPorEmail(String email) throws IOException {
        String endpoint = "/usuarios?email=eq." + email + "&select=*";

        System.out.println("Buscando usuario en: " + SupabaseConfig.REST_URL + endpoint);

        Request request = getBaseRequest(endpoint)
                .get()
                .build();

        String response = executeRequest(request);

        System.out.println("Respuesta de Supabase: " + response);

        // Parsear respuesta (es un array)
        Usuario[] usuarios = gson.fromJson(response, Usuario[].class);

        System.out.println("Usuarios parseados: " + (usuarios != null ? usuarios.length : "null"));

        return (usuarios != null && usuarios.length > 0) ? usuarios[0] : null;
    }

    /**
     * Logout
     */
    public void logout() {
        Session.getInstance().logout();
    }

    /**
     * Verificar si hay sesión activa
     */
    public boolean isLoggedIn() {
        return Session.getInstance().isLoggedIn();
    }

    /**
     * Obtener usuario actual
     */
    public Usuario getUsuarioActual() {
        return Session.getInstance().getUsuarioActual();
    }
}