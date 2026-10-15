package com.ispc.testace.ui.examenes.creados;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.testace.R;
import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.ExamenCatalogo;
import com.ispc.testace.data.service.ApiService;
import com.ispc.testace.ui.examenes.VistaPreviaExamenes;
import com.ispc.testace.ui.examenes.modificar.ModificarExamenActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisExamenesActivity extends AppCompatActivity {

    private ApiService api;
    private MiExamenAdapter adapter;
    private ProgressBar progress;
    private TextView tvVacio;
    private Call<List<ExamenCatalogo>> llamadaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_examenes);
        api = ApiClient.getApiService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progress = findViewById(R.id.progressMisExamenes);
        tvVacio = findViewById(R.id.tvVacioMisExamenes);

        adapter = new MiExamenAdapter(new MiExamenAdapter.Listener() {
            @Override
            public void onVerMas(ExamenCatalogo examen) {
                abrirVistaPrevia(examen);
            }

            @Override
            public void onEditar(ExamenCatalogo examen) {
                Intent i = new Intent(MisExamenesActivity.this, ModificarExamenActivity.class);
                i.putExtra("examen_slug", examen.getSlug());
                startActivity(i);
            }

            @Override
            public void onEliminar(ExamenCatalogo examen) {
                confirmarEliminar(examen);
            }
        });

        RecyclerView rv = findViewById(R.id.rvMisExamenes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // También refresca al volver de "Modificar examen"
        cargarExamenes();
    }

    private void cargarExamenes() {
        if (llamadaActual != null) llamadaActual.cancel();

        progress.setVisibility(View.VISIBLE);
        tvVacio.setVisibility(View.GONE);

        llamadaActual = api.getMisExamenes(true);
        llamadaActual.enqueue(new Callback<List<ExamenCatalogo>>() {
            @Override
            public void onResponse(Call<List<ExamenCatalogo>> call,
                                   Response<List<ExamenCatalogo>> r) {
                progress.setVisibility(View.GONE);
                if (r.isSuccessful() && r.body() != null) {
                    adapter.setItems(r.body());
                    tvVacio.setVisibility(r.body().isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    Toast.makeText(MisExamenesActivity.this,
                            "No se pudieron cargar tus exámenes (código " + r.code() + ")",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ExamenCatalogo>> call, Throwable t) {
                if (call.isCanceled()) return;
                progress.setVisibility(View.GONE);
                Toast.makeText(MisExamenesActivity.this,
                        "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmarEliminar(ExamenCatalogo examen) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("¿Está seguro de que quiere eliminar este examen?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar", (d, w) -> eliminarExamen(examen))
                .create();

        // Los colores de los botones se aplican cuando el diálogo ya se mostró
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.gris_cancelar));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(this, R.color.rojo_eliminar));
        });
        dialog.show();
    }

    private void eliminarExamen(ExamenCatalogo examen) {
        api.deleteExamen(examen.getSlug()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> r) {
                if (r.isSuccessful()) {
                    Toast.makeText(MisExamenesActivity.this,
                            "Examen eliminado", Toast.LENGTH_SHORT).show();
                    cargarExamenes();
                } else {
                    Toast.makeText(MisExamenesActivity.this,
                            "No se pudo eliminar el examen (código " + r.code() + ")",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MisExamenesActivity.this,
                        "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirVistaPrevia(ExamenCatalogo examen) {
        Intent intent = new Intent(this, VistaPreviaExamenes.class);
        intent.putExtra("EXAMEN_ID", String.valueOf(examen.getId()));
        intent.putExtra("EXAMEN_SLUG", examen.getSlug());
        intent.putExtra("EXAMEN_NOMBRE", examen.getTitulo());
        intent.putExtra("EXAMEN_DESCRIPCION", examen.getDescripcion());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (llamadaActual != null) llamadaActual.cancel();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}