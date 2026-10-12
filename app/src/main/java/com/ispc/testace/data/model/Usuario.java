package com.ispc.testace.data.model;

public class Usuario {
    private int id;
    private String email;
    private String nombre;
    private String apellido;
    private String rol;

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
}

