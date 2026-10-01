package com.ispc.testace.data.service;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.RespuestaUsuario;

import retrofit2.Call;
import retrofit2.Callback;

public class RespuestaUsuarioService {
    private final ApiService apiService;

    public RespuestaUsuarioService(){
        this.apiService = ApiClient.getApiService();
    }

    public void createrespuestaUsuario(RespuestaUsuario respuesta, Callback<RespuestaUsuario> callback){
        apiService.createRespuestaUsuario(respuesta).enqueue(callback);
    }
}
