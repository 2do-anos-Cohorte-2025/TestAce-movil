package com.ispc.testace.data.repository;

import com.google.gson.Gson;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.service.IntentoExamenService;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class IntentoExamenRepository {
    private final IntentoExamenService intentoExamenService;


    public IntentoExamenRepository() {
        this.intentoExamenService = new IntentoExamenService();
    }



    public void createIntentoExamen(IntentoExamen intentoExamen, Callback<IntentoExamen> callback) {
        // Transformar el objeto a JSON en el Repository
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("examen", intentoExamen.getExamen());
        requestBody.put("usuario", intentoExamen.getUsuario());

        Gson gson = new Gson();
        String json = gson.toJson(requestBody);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        intentoExamenService.createIntentoExamen(body, callback);
    }

    public void updateIntentoExamen( IntentoExamen intentoExamen, Callback<IntentoExamen> callback) {
        // Transformar el objeto a JSON en el Repository
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fecha_fin", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(intentoExamen.getFechaFin()));

        Gson gson = new Gson();
        String json = gson.toJson(requestBody);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        intentoExamenService.updateIntentoExamen(intentoExamen.getId(),body, callback);
    }


    public void getIntentosExamen(int examenId, Callback<List<IntentoExamen>> callback) {
        intentoExamenService.getIntentosExamen(examenId, callback);
    }

    public void deleteIntentoExamen(int examenId, Callback<IntentoExamen> callback) {
        intentoExamenService.deleteIntentoExamen(examenId, callback);
    }
}