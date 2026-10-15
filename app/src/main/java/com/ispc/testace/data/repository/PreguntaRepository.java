package com.ispc.testace.data.repository;

import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.service.PreguntaService;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;
public class PreguntaRepository {
    private final PreguntaService preguntaService;

    public PreguntaRepository() {
        this.preguntaService = new PreguntaService();
    }

    public void getPreguntasByExamen(int examenId, Callback<List<Pregunta>> callback) {
        preguntaService.getPreguntasByExamen(examenId, callback);
    }

    public void updatePregunta(int preguntaId, Map<String, Object> datos, Callback<Pregunta> callback) {

        preguntaService.updatePregunta(preguntaId, datos, callback);
    }

}
