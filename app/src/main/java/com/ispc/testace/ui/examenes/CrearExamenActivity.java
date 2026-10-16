
package com.ispc.testace.ui.examenes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ispc.testace.R;
import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.service.ExamenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;

public class CrearExamenActivity extends AppCompatActivity {

    private EditText edtTitulo;
    private EditText edtDescripcion;
    private EditText edtTiempoLimite;
    private Button btnCrear;

    private ExamenService examenService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_examen);
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());

        edtTitulo = findViewById(R.id.edtTituloExamen);
        edtDescripcion = findViewById(R.id.edtDescripcionExamen);
        edtTiempoLimite = findViewById(R.id.edtTiempoLimite);
        btnCrear = findViewById(R.id.btnCrearExamen);

        examenService = new ExamenService();

        btnCrear.setOnClickListener(v -> crearExamen());
    }

    private void crearExamen() {
        String titulo = edtTitulo.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();
        String tiempoTexto = edtTiempoLimite.getText().toString().trim();

        if (titulo.isEmpty() || descripcion.isEmpty()
                || tiempoTexto.isEmpty()) {
            Toast.makeText(this, "Completá todos los campos",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int tiempoLimite;

        try {
            tiempoLimite = Integer.parseInt(tiempoTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingresá un tiempo válido",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (tiempoLimite <= 0) {
            Toast.makeText(this, "El tiempo debe ser mayor que cero",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Examen examen = new Examen();
        examen.setTitulo(titulo);
        examen.setDescripcion(descripcion);
        examen.setTiempoLimite(tiempoLimite);

        btnCrear.setEnabled(false);

        examenService.crearExamen(examen, new Callback<Examen>() {
            @Override
            public void onResponse(Call<Examen> call,
                                   Response<Examen> response) {
                btnCrear.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CrearExamenActivity.this,
                            "Examen creado correctamente", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(
                            CrearExamenActivity.this,
                            CrearPreguntasActivity.class
                    );

                    intent.putExtra("EXAMEN_ID", response.body().getId());

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CrearExamenActivity.this,
                            "No se pudo crear el examen. Código: "
                                    + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Examen> call, Throwable t) {
                btnCrear.setEnabled(true);
                Toast.makeText(CrearExamenActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
