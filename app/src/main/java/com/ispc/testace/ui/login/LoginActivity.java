package com.ispc.testace.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ispc.testace.R;
import com.ispc.testace.ui.registro.RegisterActivity;
import com.ispc.testace.ui.home.HomeActivity;
public class LoginActivity extends AppCompatActivity {

    // Declaramos los componentes
    private EditText etUsuario;
    private EditText etPassword;
    private Button btnLogin;
    private String textoBotonLogin;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //  Enlazar componentes Java con su ID XML
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        textoBotonLogin = btnLogin.getText().toString();

        //  Observar el ViewModel
        viewModel.isLoading().observe(this, cargando -> {
            btnLogin.setEnabled(!cargando);
            btnLogin.setText(cargando ? "Ingresando..." : textoBotonLogin);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                viewModel.limpiarError();
            }
        });

        viewModel.getLoginExitoso().observe(this, exitoso -> {
            if (Boolean.TRUE.equals(exitoso)) {
                Toast.makeText(this, "Iniciaste sesión", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });

        //  Click en "Ingresar"
        btnLogin.setOnClickListener(v -> {
            String email = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.login(email, password);
        });

        TextView tvIrARegistro = findViewById(R.id.tvIrARegistro);
        tvIrARegistro.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        Button btnOmitirLogin = findViewById(R.id.btnOmitirLogin);
        btnOmitirLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}