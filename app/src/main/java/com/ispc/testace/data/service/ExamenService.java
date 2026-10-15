package com.ispc.testace.data.service;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.service.ApiService;
import com.ispc.testace.data.model.ExamenCatalogo;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Callback;


public class ExamenService {
    private final ApiService apiService;

    public ExamenService() {
        this.apiService = ApiClient.getApiService();
    }

    public void getExamenBySlug(String slug, Callback<Examen> callback) {
        apiService.getExamenBySlug(slug).enqueue(callback);
    }


    public void updateExamen(String slug, Map<String, Object> datos, Callback<Examen> callback) {
        apiService.updateExamen(slug, datos).enqueue(callback);
    }

    public void getExamenes(Callback<List<ExamenCatalogo>> callback) {
        apiService.getExamenesCatalogo(null, null, null, null).enqueue(callback);
    }

    public void deleteExamen(String slug, Callback<Void> callback) {
        apiService.deleteExamen(slug).enqueue(callback);
    }
}
