package com.ispc.testace.data.service;

import android.util.Log;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.RespuestaUsuario;

import retrofit2.Callback;

public class RespuestaUsuarioService {
    private final ApiService apiService;

    public RespuestaUsuarioService(){
        this.apiService = ApiClient.getApiService();
    }

    public void createRespuestaUsuario(RespuestaUsuario respuesta, Callback<RespuestaUsuario> callback) {
        apiService.createRespuestaUsuario(respuesta).enqueue(callback);
    }
    public void deleteRespuestaUsuario(int respuestaId, Callback<Void> callback) {
        apiService.deleteRespuestaUsuario(respuestaId).enqueue(callback);
    }
}
