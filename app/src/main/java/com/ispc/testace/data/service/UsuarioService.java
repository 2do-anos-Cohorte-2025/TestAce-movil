package com.ispc.testace.data.service;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public class UsuarioService {
    private final ApiService apiService;

    public UsuarioService() {
        this.apiService = ApiClient.getApiService();
    }

    public void getUsuarios(Callback<List<Usuario>> callback) {
        apiService.getUsuarios().enqueue(callback);
    }

    public void createUsuario(Map<String, Object> datos, Callback<Usuario> callback) {
        apiService.createUsuario(datos).enqueue(callback);
    }

    public void updateUsuario(int id, Map<String, Object> datos, Callback<Usuario> callback) {
        apiService.updateUsuario(id, datos).enqueue(callback);
    }

    public void deleteUsuario(int id, Callback<Void> callback) {
        apiService.deleteUsuario(id).enqueue(callback);
    }
}
