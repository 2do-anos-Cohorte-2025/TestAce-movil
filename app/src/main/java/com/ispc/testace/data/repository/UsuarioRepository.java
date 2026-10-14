package com.ispc.testace.data.repository;

import com.ispc.testace.data.model.Usuario;
import com.ispc.testace.data.service.UsuarioService;

import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public class UsuarioRepository {

    private final UsuarioService usuarioService;

    public UsuarioRepository() {
        this.usuarioService = new UsuarioService();
    }

    public void getUsuarios(Callback<List<Usuario>> callback) {
        usuarioService.getUsuarios(callback);
    }

    public void createUsuario(Map<String, Object> datos, Callback<Usuario> callback) {
        usuarioService.createUsuario(datos, callback);
    }

    public void updateUsuario(int id, Map<String, Object> datos, Callback<Usuario> callback) {
        usuarioService.updateUsuario(id, datos, callback);
    }

    public void deleteUsuario(int id, Callback<Void> callback) {
        usuarioService.deleteUsuario(id, callback);
    }
}
