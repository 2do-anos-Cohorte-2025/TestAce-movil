package com.ispc.testace.data.repository;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.service.OpcionService;
import java.util.List;
import retrofit2.Callback;
public class OpcionRepository {
    private final OpcionService opcionService;

    public OpcionRepository(){
        this.opcionService= new OpcionService();
    }

    public void getOpcionesByPregunta(int preguntaId, Callback<List<Opcion>> callback){
        opcionService.getOpcionesByPregunta(preguntaId, callback);
    }

}
