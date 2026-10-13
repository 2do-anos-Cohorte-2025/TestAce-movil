package com.ispc.testace.data.repository;

import com.google.gson.Gson;
import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.service.ExamenService;
import com.ispc.testace.data.service.IntentoExamenService;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class ExamenRepository {

    private final ExamenService examenService;


    public ExamenRepository() {
        this.examenService = new ExamenService();
    }


    public void getExamenBySlug(String slug, Callback<Examen> callback) {
        examenService.getExamenBySlug(slug, callback);
    }

    public void updateExamen(String slug, Map<String, Object> datos, Callback<Examen> callback) {
        examenService.updateExamen(slug, datos, callback);
    }
}
