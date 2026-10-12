package com.ispc.testace.data.service;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.IntentoExamen;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Callback;
public class IntentoExamenService {
    private final ApiService apiService;

    public IntentoExamenService() {
        this.apiService = ApiClient.getApiService();
    }

    public void createIntentoExamen(RequestBody requestBody, Callback<IntentoExamen> callback) {
        apiService.createIntentoExamen(requestBody).enqueue(callback);
    }

    public void updateIntentoExamen(int id,RequestBody intentoExamen, Callback<IntentoExamen> callback) {
        apiService.updateIntentoExamen(id, intentoExamen).enqueue(callback);
    }



    public void getIntentosExamen(int examenId, Callback<List<IntentoExamen>> callback) {
        apiService.getIntentosExamen(examenId).enqueue(callback);
    }

    public void deleteIntentoExamen(int examenId, Callback<IntentoExamen> callback) {
        apiService.deleteIntentoExamen(examenId).enqueue(callback);
    }
}
