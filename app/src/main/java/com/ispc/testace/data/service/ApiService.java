package com.ispc.testace.data.service;


import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.model.RespuestaUsuario;

import com.ispc.testace.data.model.RegistroRequest;
import com.ispc.testace.data.model.RegistroResponse;
import com.ispc.testace.data.model.LoginRequest;
import com.ispc.testace.data.model.AuthResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface  ApiService {
    @GET("examenes/")
    Call<List<Examen>> getExamenes();

    @POST("examenes/")
    Call<Examen> createExamen(@Body Examen examen);

    @POST("auth/registro/")
    Call<RegistroResponse> registro(@Body RegistroRequest body);

    @GET("intentos/")
    Call<List<IntentoExamen>> getIntentosExamen(@Query("examen") int examenId);

    @POST("intentos/")
    Call<IntentoExamen> createIntentoExamen(@Body RequestBody requestBody);



    @PUT("intentos/{id}/")
    Call<IntentoExamen> updateIntentoExamen(@Path("id") int id, @Body RequestBody intentoExamen);

    @GET("opciones/")
    Call<List<Opcion>> getOpcionesByPregunta(@Query("pregunta_id") int preguntaId);

    @GET("preguntas/")
    Call<List<Pregunta>> getPreguntasByExamen(@Query("examen_id") int examenId);

    @POST("respuestas/")
    Call<RespuestaUsuario> createRespuestaUsuario(@Body RespuestaUsuario respuesta);

    @DELETE("intentos/{id}/")
    Call<IntentoExamen> deleteIntentoExamen(@Path("id") int intentoExamenId);

    @POST("auth/login/")
    Call<AuthResponse> login(@Body LoginRequest request);
}
