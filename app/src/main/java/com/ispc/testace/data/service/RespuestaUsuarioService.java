package com.ispc.testace.data.service;

import com.google.gson.Gson;
import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.RespuestaUsuario;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RespuestaUsuarioService {
    private final ApiService apiService;

    public RespuestaUsuarioService(){
        this.apiService = ApiClient.getApiService();
    }

    public void createRespuestaUsuario(RespuestaUsuario respuesta, Callback<RespuestaUsuario> callback) {
        apiService.createRespuestaUsuario(respuesta).enqueue(callback);
    }
}
