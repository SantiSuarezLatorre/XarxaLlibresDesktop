package com.xarxallibres.desktop.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xarxallibres.desktop.config.SupabaseConfig;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class SupabaseService {
    protected static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    protected static final OkHttpClient client = new OkHttpClient();
    protected static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    protected Request.Builder getBaseRequest(String endpoint) {
        return new Request.Builder()
                .url(SupabaseConfig.REST_URL + endpoint)
                .addHeader("apikey", SupabaseConfig.SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SupabaseConfig.SUPABASE_KEY);
    }

    protected Request.Builder getAuthenticatedRequest(String endpoint, String token) {
        return new Request.Builder()
                .url(SupabaseConfig.REST_URL + endpoint)
                .addHeader("apikey", SupabaseConfig.SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + token);
    }

    protected String executeRequest(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                throw new IOException("Request failed: " + response.code() + " - " + errorBody);
            }
            return response.body() != null ? response.body().string() : "";
        }
    }
}