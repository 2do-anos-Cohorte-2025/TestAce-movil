package com.ispc.testace.data.repository;

import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.service.IntentoExamenService;
import java.util.List;
import retrofit2.Callback;

public class IntentoExamenRepository {

    private final  IntentoExamenService intentoExamenService;

    public IntentoExamenRepository(){
        this.intentoExamenService= new IntentoExamenService();
    }

    public void  createIntentoExamen(IntentoExamen intentoExamen, Callback<IntentoExamen> callback){
        intentoExamenService.createIntentoExamen(intentoExamen, callback);
    }


    public void updateIntentoExamen(int id, IntentoExamen intentoExamen, Callback<IntentoExamen> callback){
        intentoExamenService.updateIntentoExamen(id, intentoExamen,callback);
    }

    public void getIntentosExamen(int examenId, Callback<List<IntentoExamen>>  callback){
        intentoExamenService.getIntentosExamen(examenId, callback);
    }

    public void deleteIntentoExamen(int examenId, IntentoExamen intentoExamen, Callback<IntentoExamen> callback){
        intentoExamenService.deleteIntentoExamen(examenId, intentoExamen,callback);
    }
}
