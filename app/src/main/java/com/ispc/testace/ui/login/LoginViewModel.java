package com.ispc.testace.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ispc.testace.data.model.AuthResponse;
import com.ispc.testace.data.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private final AuthRepository authRepository;

    // Datos observables
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginExitoso = new MutableLiveData<>(false);

    public LoginViewModel() {
        this.authRepository = new AuthRepository();
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getLoginExitoso() {
        return loginExitoso;
    }

    public void limpiarError() {
        errorMessage.setValue(null);
    }

    public void login(String email, String password) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        authRepository.login(email, password, new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    loginExitoso.setValue(true);
                } else if (response.code() == 400 || response.code() == 401) {
                    errorMessage.setValue("Email o contraseña incorrectos");
                } else {
                    errorMessage.setValue("Error del servidor (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("No se pudo conectar con el servidor");
            }
        });
    }
}
