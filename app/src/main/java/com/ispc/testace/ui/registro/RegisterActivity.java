package com.ispc.testace.ui.registro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.ispc.testace.R;
import com.ispc.testace.ui.login.LoginActivity;

import android.util.Patterns;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.RegistroRequest;
import com.ispc.testace.data.model.RegistroResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etNombre;
    private TextInputEditText etApellido;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmarPassword;
    private Button btnRegistrarse;
    private TextInputLayout tilNombre, tilApellido, tilEmail, tilPassword, tilConfirmarPassword;

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
        tilNombre = findViewById(R.id.tilNombre);
        tilApellido = findViewById(R.id.tilApellido);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmarPassword = findViewById(R.id.tilConfirmarPassword);

        btnRegistrarse.setOnClickListener(v -> intentarRegistro());
        TextView tvIrALogin = findViewById(R.id.tvIrALogin);
        tvIrALogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void intentarRegistro() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmar = etConfirmarPassword.getText().toString();

        tilNombre.setError(null);
        tilApellido.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmarPassword.setError(null);

        boolean ok = true;
        if (nombre.isEmpty()) {
            tilNombre.setError("Ingresá tu nombre");
            ok = false;
        }
        if (apellido.isEmpty()) {
            tilApellido.setError("Ingresá tu apellido");
            ok = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email inválido");
            ok = false;
        }
        if (password.length() < 8) {
            tilPassword.setError("Mínimo 8 caracteres");
            ok = false;
        }
        if (!password.equals(confirmar)) {
            tilConfirmarPassword.setError("Las contraseñas no coinciden");
            ok = false;
        }
        if (!ok) return;

        btnRegistrarse.setEnabled(false);   // evita el doble tap
        RegistroRequest body = new RegistroRequest(nombre, apellido, email, password, confirmar);

        ApiClient.getApiService().registro(body).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                btnRegistrarse.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Cuenta creada. Iniciá sesión.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, extraerError(response), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable t) {
                btnRegistrarse.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    // El backend responde errores como {"email": ["..."]} o {"password": "..."}
    private String extraerError(Response<?> response) {
        try {
            JSONObject json = new JSONObject(response.errorBody().string());
            Iterator<String> keys = json.keys();
            if (keys.hasNext()) {
                Object valor = json.get(keys.next());
                if (valor instanceof JSONArray) return ((JSONArray) valor).getString(0);
                return valor.toString();
            }
        } catch (Exception ignorada) {
        }
        return "No se pudo completar el registro";
    }
}