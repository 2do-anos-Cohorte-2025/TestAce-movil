package com.ispc.testace.data.service;


import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.Pregunta;

import java.util.List;

import retrofit2.Callback;

public class OpcionService {

    private final ApiService apiService;

    public OpcionService(){
        this.apiService= ApiClient.getApiService();
    }
    public void getOpcionesByPregunta(int preguntaId, Callback<List<Opcion>> callback) {
        apiService.getOpcionesByPregunta(preguntaId).enqueue(callback);
    }

}
