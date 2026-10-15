package com.ispc.testace.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Perfil {
    private int id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String email;
    private String rol;

    @SerializedName("imagen_usuario")
    private String imagenUsuario;

    private List<IntentoExamen> intentos;

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getImagenUsuario() { return imagenUsuario; }
    public List<IntentoExamen> getIntentos() { return intentos; }
}