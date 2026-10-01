package com.ispc.testace.data.service;


import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.model.RespuestaUsuario;



import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
public interface  ApiService {
    @GET("examenes/")
    Call<List<Examen>> getExamenes();

    @POST("examenes/")
    Call<Examen> createExamen(@Body Examen examen);


    @GET("intentos-examen/")
    Call<List<IntentoExamen>> getIntentosExamen(@Query("examen") int examenId);

    @POST("intentos-examen/")
    Call<IntentoExamen> createIntentoExamen(@Body IntentoExamen intentoExamen);


    //Probar put y patch,
    @PUT("intentos-examen/{id}/")
    Call<IntentoExamen> updateIntentoExamen(
            @Path("id") int id,
            @Body IntentoExamen intentoExamen
    );


    @GET("opciones/")
    Call<List<Opcion>> getOpcionesByPregunta(@Query("pregunta_id") int preguntaId);

    @GET("preguntas/")
    Call<List<Pregunta>> getPreguntasByExamen (@Query("examen_id") int examenId);

    @POST("respuestas-usuario/")
    Call<RespuestaUsuario> createRespuestaUsuario(@Body RespuestaUsuario respuesta);

    @DELETE("intentos-examen/{id}")
    Call<IntentoExamen> deleteIntentoExamen(@Query("intento_examen_id") int intentoExamenId);


}
