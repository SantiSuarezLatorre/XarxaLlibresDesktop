package com.xarxallibres.desktop.services;

import com.xarxallibres.desktop.models.Curso;
import com.xarxallibres.desktop.models.Session;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CursoService extends SupabaseService {

    /**
     * Obtener todos los cursos activos
     */
    public List<Curso> obtenerCursosActivos() throws IOException {
        String endpoint = "/cursos?activo=eq.true&order=nivel.asc,nombre.asc";

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        Curso[] cursos = gson.fromJson(response, Curso[].class);

        return cursos != null ? Arrays.asList(cursos) : new ArrayList<>();
    }

    /**
     * Obtener todos los cursos (incluyendo inactivos)
     */
    public List<Curso> obtenerTodosCursos() throws IOException {
        String endpoint = "/cursos?order=nivel.asc,nombre.asc";

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        Curso[] cursos = gson.fromJson(response, Curso[].class);

        return cursos != null ? Arrays.asList(cursos) : new ArrayList<>();
    }

    /**
     * Obtener curso por ID
     */
    public Curso obtenerCursoPorId(String id) throws IOException {
        String endpoint = "/cursos?id=eq." + id;

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        Curso[] cursos = gson.fromJson(response, Curso[].class);

        return (cursos != null && cursos.length > 0) ? cursos[0] : null;
    }

    /**
     * Crear nuevo curso
     */
    public Curso crearCurso(Curso curso) throws IOException {
        String endpoint = "/cursos";

        String json = gson.toJson(curso);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .post(body)
                .addHeader("Prefer", "return=representation")
                .build();

        String response = executeRequest(request);
        Curso[] cursos = gson.fromJson(response, Curso[].class);

        return (cursos != null && cursos.length > 0) ? cursos[0] : null;
    }

    /**
     * Actualizar curso
     */
    public Curso actualizarCurso(String id, Curso curso) throws IOException {
        String endpoint = "/cursos?id=eq." + id;

        String json = gson.toJson(curso);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .patch(body)
                .addHeader("Prefer", "return=representation")
                .build();

        String response = executeRequest(request);
        Curso[] cursos = gson.fromJson(response, Curso[].class);

        return (cursos != null && cursos.length > 0) ? cursos[0] : null;
    }

    /**
     * Eliminar curso (soft delete)
     */
    public void desactivarCurso(String id) throws IOException {
        String endpoint = "/cursos?id=eq." + id;

        Curso cursoUpdate = new Curso();
        cursoUpdate.setActivo(false);

        String json = gson.toJson(cursoUpdate);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .patch(body)
                .build();

        executeRequest(request);
    }
}