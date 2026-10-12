package com.ispc.testace.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ispc.testace.data.model.AuthResponse;
import com.ispc.testace.data.model.Usuario;

public class TokenManager {
    private static final String PREFS = "testace_session";
    private static final String KEY_ACCESS = "access_token";
    private static final String KEY_REFRESH = "refresh_token";
    private static final String KEY_USUARIO = "usuario";

    private static final Gson gson = new Gson();
    private static SharedPreferences prefs;

    private TokenManager() {
    }

    public static void init(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void guardarSesion(AuthResponse respuesta) {
        if (prefs == null || respuesta == null) return;
        prefs.edit()
                .putString(KEY_ACCESS, respuesta.getAccess())
                .putString(KEY_REFRESH, respuesta.getRefresh())
                .putString(KEY_USUARIO, gson.toJson(respuesta.getUsuario()))
                .apply();
    }

    public static void cerrarSesion() {
        if (prefs == null) return;
        prefs.edit().clear().apply();
    }

    public static String getAccessToken() {
        return prefs == null ? null : prefs.getString(KEY_ACCESS, null);
    }

    public static String getRefreshToken() {
        return prefs == null ? null : prefs.getString(KEY_REFRESH, null);
    }

    public static Usuario getUsuario() {
        if (prefs == null) return null;
        String json = prefs.getString(KEY_USUARIO, null);
        return json == null ? null : gson.fromJson(json, Usuario.class);
    }

    public static Integer getUsuarioId() {
        Usuario u = getUsuario();
        return (u != null && u.getId() > 0) ? u.getId() : null;
    }

    public static boolean estaLogueado() {
        String token = getAccessToken();
        return token != null && !token.isEmpty();
    }
}

