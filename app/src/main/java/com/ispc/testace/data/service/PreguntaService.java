package com.ispc.testace.data.service;


import com.ispc.testace.data.ApiClient;
import  com.ispc.testace.data.model.Pregunta;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
public class PreguntaService {
    private final ApiService apiService;
    public  PreguntaService(){
        this.apiService = ApiClient.getApiService();
    }

    public void getPreguntasByExamen(int examenId, Callback<List<Pregunta>> callback){
        apiService.getPreguntasByExamen(examenId).enqueue(callback);
    }
}
