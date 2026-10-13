package com.ispc.testace.data.service;


import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.model.RespuestaUsuario;
import com.ispc.testace.data.model.LoginRequest;
import com.ispc.testace.data.model.AuthResponse;
import com.ispc.testace.data.model.ExamenCatalogo;
import com.ispc.testace.data.model.FiltroItem;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface  ApiService {

    /* Examen Endoints */
    @GET("examenes/{slug}/")
    Call<Examen> getExamenBySlug(
            @Path("slug") String slug
    );

    @POST("examenes/")
    Call<Examen> createExamen(@Body Examen examen);

    @PATCH("examenes/{slug}/")
    Call<Examen> updateExamen(@Path("slug") String slug, @Body Map<String, Object> datos);

    /* Intento Examen Endoints */
    @GET("intentos/")
    Call<List<IntentoExamen>> getIntentosExamen(@Query("examen") int examenId);

    @POST("intentos/")
    Call<IntentoExamen> createIntentoExamen(@Body RequestBody requestBody);

    @PUT("intentos/{id}/")
    Call<IntentoExamen> updateIntentoExamen(@Path("id") int id, @Body RequestBody intentoExamen);

    @DELETE("intentos/{id}/")
    Call<IntentoExamen> deleteIntentoExamen(@Path("id") int intentoExamenId);


    /* Preguntas Endoints */
    @GET("preguntas/")
    Call<List<Pregunta>> getPreguntasByExamen(@Query("examen_id") int examenId);

    @PATCH("preguntas/{id}/")
    Call<Pregunta> updatePregunta(@Path("id") int id, @Body Map<String, Object> datos);

    /* Opciones Endoints */
    @GET("opciones/")
    Call<List<Opcion>> getOpcionesByPregunta(@Query("pregunta_id") int preguntaId);

    @PATCH("opciones/{id}/")
    Call<Opcion> updateOpcion(@Path("id") int id, @Body Map<String, Object> datos);
    /* Respuestas Endoints */
    @POST("respuestas/")
    Call<RespuestaUsuario> createRespuestaUsuario(@Body RespuestaUsuario respuesta);


    /*Autenticación Endpoints */
    @POST("auth/login/")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("examenes/")
    Call<List<ExamenCatalogo>> getExamenesCatalogo(
            @Query("categoria") String categoria,
            @Query("nivel") String nivel,
            @Query("creador") String creador,
            @Query("search") String search);

    @GET("categorias/")
    Call<List<FiltroItem>> getCategorias();

    @GET("niveles/")
    Call<List<FiltroItem>> getNiveles();
}
