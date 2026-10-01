package com.ispc.testace.data.service;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.IntentoExamen;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
public class IntentoExamenService {
    private final ApiService apiService;

    public  IntentoExamenService(){
        this.apiService = ApiClient.getApiService();
    }

    public void createIntentoExamen(IntentoExamen intentoExamen, Callback<IntentoExamen> callback){
        apiService.createIntentoExamen(intentoExamen).enqueue(callback);
    }

    public void updateIntentoExamen(int id, IntentoExamen intentoExamen, Callback<IntentoExamen> callback){
        apiService.updateIntentoExamen(id, intentoExamen).enqueue(callback);
    }

    public void getIntentosExamen(int examenId, Callback<List<IntentoExamen>> callback){
        apiService.getIntentosExamen(examenId).enqueue(callback);
    }

    public void deleteIntentoExamen(int examenId, IntentoExamen intentoExamen, Callback<IntentoExamen> callback){
        apiService.deleteIntentoExamen(examenId).enqueue(callback);
    }

}
