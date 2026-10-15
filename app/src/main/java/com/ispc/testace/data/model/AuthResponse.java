package com.ispc.testace.data.model;

public class AuthResponse {
    private String access;
    private String refresh;
    private Usuario usuario;

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

