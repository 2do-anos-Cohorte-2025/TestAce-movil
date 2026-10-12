package com.ispc.testace.data.service;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.AuthResponse;
import com.ispc.testace.data.model.LoginRequest;

import retrofit2.Callback;

public class AuthService {
    private final ApiService apiService;

    public AuthService() {
        this.apiService = ApiClient.getApiService();
    }

    public void login(LoginRequest request, Callback<AuthResponse> callback) {
        apiService.login(request).enqueue(callback);
    }

}
