package com.xarxallibres.desktop.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xarxallibres.desktop.models.Libro;
import com.xarxallibres.desktop.models.Session;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibroService extends SupabaseService {

    /**
     * Obtener todos los libros con información del curso
     */
    public List<Libro> obtenerTodosLibros() throws IOException {
        String endpoint = "/libros?select=*,cursos(nombre)&order=titulo.asc";

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        return parseLibrosConCurso(response);
    }

    /**
     * Obtener libros por curso
     */
    public List<Libro> obtenerLibrosPorCurso(String cursoId) throws IOException {
        String endpoint = "/libros?curso_id=eq." + cursoId + "&select=*,cursos(nombre)&order=asignatura.asc,titulo.asc";

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        return parseLibrosConCurso(response);
    }

    /**
     * Obtener libro por ID
     */
    public Libro obtenerLibroPorId(String id) throws IOException {
        String endpoint = "/libros?id=eq." + id + "&select=*,cursos(nombre)";

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        List<Libro> libros = parseLibrosConCurso(response);

        return (libros != null && !libros.isEmpty()) ? libros.get(0) : null;
    }

    /**
     * Crear nuevo libro
     */
    public Libro crearLibro(Libro libro) throws IOException {
        String endpoint = "/libros";

        String json = gson.toJson(libro);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .post(body)
                .addHeader("Prefer", "return=representation")
                .build();

        String response = executeRequest(request);
        Libro[] libros = gson.fromJson(response, Libro[].class);

        return (libros != null && libros.length > 0) ? libros[0] : null;
    }

    /**
     * Actualizar libro
     */
    public Libro actualizarLibro(String id, Libro libro) throws IOException {
        String endpoint = "/libros?id=eq." + id;

        // Crear objeto solo con campos a actualizar
        JsonObject updateData = new JsonObject();
        if (libro.getIsbn() != null) updateData.addProperty("isbn", libro.getIsbn());
        if (libro.getTitulo() != null) updateData.addProperty("titulo", libro.getTitulo());
        if (libro.getEditorial() != null) updateData.addProperty("editorial", libro.getEditorial());
        if (libro.getAutor() != null) updateData.addProperty("autor", libro.getAutor());
        if (libro.getAsignatura() != null) updateData.addProperty("asignatura", libro.getAsignatura());
        if (libro.getCursoId() != null) updateData.addProperty("curso_id", libro.getCursoId());
        updateData.addProperty("stock_total", libro.getStockTotal());
        updateData.addProperty("stock_disponible", libro.getStockDisponible());
        if (libro.getPrecioAproximado() != null) {
            updateData.addProperty("precio_aproximado", libro.getPrecioAproximado());
        }
        if (libro.getImagenUrl() != null) updateData.addProperty("imagen_url", libro.getImagenUrl());
        updateData.addProperty("activo", libro.isActivo());

        String json = gson.toJson(updateData);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .patch(body)
                .addHeader("Prefer", "return=representation")
                .build();

        String response = executeRequest(request);
        Libro[] libros = gson.fromJson(response, Libro[].class);

        return (libros != null && libros.length > 0) ? libros[0] : null;
    }

    /**
     * Eliminar libro (soft delete)
     */
    public void desactivarLibro(String id) throws IOException {
        String endpoint = "/libros?id=eq." + id;

        JsonObject updateData = new JsonObject();
        updateData.addProperty("activo", false);

        String json = gson.toJson(updateData);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .patch(body)
                .build();

        executeRequest(request);
    }

    /**
     * Buscar libros por ISBN
     */
    public List<Libro> buscarPorIsbn(String isbn) throws IOException {
        String endpoint = "/libros?isbn=ilike.*" + isbn + "*&select=*,cursos(nombre)";

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        return parseLibrosConCurso(response);
    }

    /**
     * Buscar libros por título
     */
    public List<Libro> buscarPorTitulo(String titulo) throws IOException {
        String endpoint = "/libros?titulo=ilike.*" + titulo + "*&select=*,cursos(nombre)";

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .get()
                .build();

        String response = executeRequest(request);
        return parseLibrosConCurso(response);
    }

    /**
     * Actualizar solo el stock
     */
    public void actualizarStock(String id, int stockTotal, int stockDisponible) throws IOException {
        String endpoint = "/libros?id=eq." + id;

        JsonObject updateData = new JsonObject();
        updateData.addProperty("stock_total", stockTotal);
        updateData.addProperty("stock_disponible", stockDisponible);

        String json = gson.toJson(updateData);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = getAuthenticatedRequest(endpoint, Session.getInstance().getAccessToken())
                .patch(body)
                .build();

        executeRequest(request);
    }

    /**
     * Parse JSON de libros con información de curso
     */
    private List<Libro> parseLibrosConCurso(String jsonResponse) {
        List<Libro> libros = new ArrayList<>();

        try {
            JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                Libro libro = gson.fromJson(jsonObject, Libro.class);

                // Extraer nombre del curso si existe
                if (jsonObject.has("cursos") && !jsonObject.get("cursos").isJsonNull()) {
                    JsonObject cursoObj = jsonObject.getAsJsonObject("cursos");
                    if (cursoObj.has("nombre")) {
                        libro.setCursoNombre(cursoObj.get("nombre").getAsString());
                    }
                }

                libros.add(libro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return libros;
    }
}