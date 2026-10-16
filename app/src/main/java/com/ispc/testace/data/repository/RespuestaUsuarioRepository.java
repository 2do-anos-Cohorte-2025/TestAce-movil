package com.ispc.testace.data.repository;

import android.util.Log;

import com.ispc.testace.data.model.RespuestaUsuario;
import com.ispc.testace.data.service.RespuestaUsuarioService;
import retrofit2.Callback;
public class RespuestaUsuarioRepository {
    private final RespuestaUsuarioService respuestaUsuarioService;

    public RespuestaUsuarioRepository(){
        this.respuestaUsuarioService=new RespuestaUsuarioService();

    }

    public void createRespuestaUsuario(RespuestaUsuario respuesta, Callback<RespuestaUsuario> callback) {
        respuestaUsuarioService.createRespuestaUsuario(respuesta, callback);
    }
    public void deleteRespuestaUsuario(int respuestaId, Callback<Void> callback) {
        respuestaUsuarioService.deleteRespuestaUsuario(respuestaId, callback);
    }
}


