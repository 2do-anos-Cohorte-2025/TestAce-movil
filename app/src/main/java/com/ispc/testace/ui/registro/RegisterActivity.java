package com.ispc.testace.ui.registro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.ispc.testace.R;
import com.ispc.testace.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etNombre;
    private TextInputEditText etApellido;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmarPassword;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        // Todo (próximo sprint): conectar con RegisterViewModel y hacer el POST al backend Django
        btnRegistrarse.setOnClickListener(v -> {
            // Sin funcionalidad todavía — solo confirma que el botón responde al toque
        });
        TextView tvIrALogin = findViewById(R.id.tvIrALogin);
        tvIrALogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish(); // opcional: cierra Registro para no acumular pantallas en la pila
        });
    }
}