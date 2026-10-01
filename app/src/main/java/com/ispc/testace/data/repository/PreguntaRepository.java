package com.ispc.testace.data.repository;

import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.service.PreguntaService;
import java.util.List;
import retrofit2.Callback;
public class PreguntaRepository {
    private final PreguntaService preguntaService;

    public PreguntaRepository() {
        this.preguntaService = new PreguntaService();
    }

    public void getPreguntasByExamen(int examenId, Callback<List<Pregunta>> callback) {
        preguntaService.getPreguntasByExamen(examenId, callback);
    }

}
