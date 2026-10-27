package com.ispc.testace.ui.examenes;

import androidx.appcompat.widget.Toolbar;
import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.service.ExamenService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ispc.testace.R;
import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.ui.examenes.intento.IntentoActivity;
import com.ispc.testace.ui.examenes.modificar.ModificarExamenActivity;

import android.view.View;
import android.widget.Toast;

public class VistaPreviaExamenes extends AppCompatActivity {

    private TextView txtTitulo;
    private TextView txtDescripcion;
    private TextView txtDuracion;
    private String examenId;
    private Integer duracion_examen;
    private String examenSlug;
    private ExamenService examenService;
    private Call<Examen> llamadaExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_previa_examenes);


        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtDuracion = findViewById(R.id.txtDuracion);
        Button btnComenzar = findViewById(R.id.btnComenzar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationContentDescription("Volver");
        toolbar.setNavigationOnClickListener(v -> finish());

        Intent intentRecibido = getIntent();
        if (intentRecibido != null) {
            examenId = intentRecibido.getStringExtra("EXAMEN_ID");
            examenSlug = intentRecibido.getStringExtra("EXAMEN_SLUG");
            String nombreExamen = intentRecibido.getStringExtra("EXAMEN_NOMBRE");
            String descExamen = intentRecibido.getStringExtra("EXAMEN_DESCRIPCION");
            duracion_examen = intentRecibido.getIntExtra("EXAMEN_TIEMPO",30);

            if (nombreExamen != null) {
                txtTitulo.setText(nombreExamen);
            }
            if (descExamen != null) {
                txtDescripcion.setText(descExamen);
            }
        }

        examenService = new ExamenService();

        if (examenSlug != null && !examenSlug.trim().isEmpty()) {
            cargarExamenDesdeBackend();
        } else {
            Toast.makeText(this, "No se recibió el slug del examen.", Toast.LENGTH_LONG).show();
        }

        btnComenzar.setOnClickListener(v -> comenzarExamen());

    }


    private void cargarExamenDesdeBackend() {
        llamadaExamen = examenService.getExamenCall(examenSlug);

        llamadaExamen.enqueue(new Callback<Examen>() {
            @Override
            public void onResponse(Call<Examen> call, Response<Examen> response) {
                if (isFinishing() || isDestroyed()) return;

                if (response.isSuccessful() && response.body() != null) {
                    Examen examen = response.body();


                    if (examen.getTitulo() != null) {
                        txtTitulo.setText(examen.getTitulo());
                    }

                    if (examen.getDescripcion() != null) {
                        txtDescripcion.setText(examen.getDescripcion());
                    }

                    if (examen.getTiempoLimite() > 0) {
                        duracion_examen = examen.getTiempoLimite();
                        txtDuracion.setText("Duración: " + duracion_examen + " minutos");
                    }

                    examenId = String.valueOf(examen.getId());
                } else {
                    Toast.makeText(
                            VistaPreviaExamenes.this,
                            "Error al cargar el examen. Código HTTP: " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<Examen> call, Throwable t) {
                if (isFinishing() || isDestroyed() || call.isCanceled()) return;

                Toast.makeText(
                        VistaPreviaExamenes.this,
                        "No se pudo conectar con el backend. Revisá la IP y la conexión.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (llamadaExamen != null) {
            llamadaExamen.cancel();
        }
        super.onDestroy();
    }

    private void comenzarExamen() {
        if (examenId == null || examenId.trim().isEmpty()) {
            Toast.makeText(this, "El examen todavía no está cargado.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Intent intent = new Intent(VistaPreviaExamenes.this, IntentoActivity.class);
            intent.putExtra("examen_id", Integer.parseInt(examenId));
            intent.putExtra("duracion_examen", duracion_examen);

            Integer usuarioId = TokenManager.getUsuarioId();
            if (usuarioId != null && TokenManager.estaLogueado()) {
                intent.putExtra("usuario_id", usuarioId);
            }

            startActivity(intent);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID del examen no es válido.", Toast.LENGTH_LONG).show();
        }
    }
}