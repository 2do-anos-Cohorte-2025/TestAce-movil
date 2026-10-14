package com.ispc.testace.data.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    private int id;
    private String email;

    @SerializedName(value = "nombre", alternate = {"first_name"})
    private String nombre;

    @SerializedName(value = "apellido", alternate = {"last_name"})
    private String apellido;
    private String rol;

    @SerializedName("is_active")
    private Boolean activo;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo == null || activo;
    }

    public String getNombreCompleto() {
        String completo = ((nombre != null ? nombre : "") + " "
                + (apellido != null ? apellido : "")).trim();
        return completo.isEmpty() ? email : completo;
    }
}

