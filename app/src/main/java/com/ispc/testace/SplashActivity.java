package com.ispc.testace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.ui.home.HomeActivity;
import com.ispc.testace.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Si ya hay sesión guardada va directo a Home, si no, al login
            Class<?> destino = TokenManager.estaLogueado()
                    ? HomeActivity.class
                    : LoginActivity.class;

            startActivity(new Intent(SplashActivity.this, destino));
            finish();

        }, 2000);
    }
}