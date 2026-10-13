package com.ispc.testace.data.repository;

import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.data.model.AuthResponse;
import com.ispc.testace.data.model.LoginRequest;
import com.ispc.testace.data.service.AuthService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final AuthService authService;

    public AuthRepository() {
        this.authService = new AuthService();
    }

    public void login(String email, String password, Callback<AuthResponse> callback) {
        authService.login(new LoginRequest(email, password), guardandoSesion(callback));
    }

    private Callback<AuthResponse> guardandoSesion(Callback<AuthResponse> callback) {
        return new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenManager.guardarSesion(response.body());
                }
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        };
    }
}

