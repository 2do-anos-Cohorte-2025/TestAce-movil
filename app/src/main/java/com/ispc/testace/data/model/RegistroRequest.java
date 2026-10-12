package com.ispc.testace.data.model;

import com.google.gson.annotations.SerializedName;

public class RegistroRequest {
    @SerializedName("first_name")
    private final String firstName;
    @SerializedName("last_name")
    private final String lastName;
    private final String email;
    private final String password;
    @SerializedName("confirmar_password")
    private final String confirmarPassword;

    public RegistroRequest(String firstName, String lastName, String email,
                           String password, String confirmarPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.confirmarPassword = confirmarPassword;
    }
}