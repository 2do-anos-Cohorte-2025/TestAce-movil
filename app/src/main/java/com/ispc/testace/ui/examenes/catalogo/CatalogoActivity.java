package com.ispc.testace.ui.examenes.catalogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;


import com.ispc.testace.R;
import com.google.android.material.chip.Chip;
import android.widget.PopupMenu;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.model.ExamenCatalogo;
import com.ispc.testace.data.model.FiltroItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CatalogoActivity extends AppCompatActivity {

    private String categoriaSel = null;
    private String nivelSel = null;
    private String creadorSel = null;

    private final List<String> categorias = new ArrayList<>();
    private final List<String> niveles = new ArrayList<>();

    private Chip chipCategoria, chipNivel, chipCreador, chipLimpiar;
    private RecyclerView rvExamenes;
    private ProgressBar progress;
    private TextView tvVacio;
    private ExamenAdapter adapter;

    private Call<List<ExamenCatalogo>> llamadaActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        chipCategoria = findViewById(R.id.chipCategoria);
        chipNivel = findViewById(R.id.chipNivel);
        chipCreador = findViewById(R.id.chipCreador);
        chipLimpiar = findViewById(R.id.chipLimpiar);
        rvExamenes = findViewById(R.id.rvExamenes);
        progress = findViewById(R.id.progressCatalogo);
        tvVacio = findViewById(R.id.tvVacio);

        adapter = new ExamenAdapter(this::abrirDetalle);
        rvExamenes.setLayoutManager(new LinearLayoutManager(this));
        rvExamenes.setAdapter(adapter);

        configurarChips();
        cargarOpcionesFiltros();
        cargarExamenes();
    }

    private void configurarChips() {
        chipCategoria.setOnClickListener(v -> {
            if (categorias.isEmpty()) {
                Toast.makeText(this, "Cargando categorías...", Toast.LENGTH_SHORT).show();
                cargarOpcionesFiltros();
                return;
            }
            mostrarMenu(v, "Todas", categorias, elegido -> {
                categoriaSel = elegido;
                chipCategoria.setText(elegido != null ? elegido : "Categoría");
                cargarExamenes();
            });
        });

        chipNivel.setOnClickListener(v -> {
            if (niveles.isEmpty()) {
                Toast.makeText(this, "Cargando niveles...", Toast.LENGTH_SHORT).show();
                cargarOpcionesFiltros();
                return;
            }
            mostrarMenu(v, "Todos", niveles, elegido -> {
                nivelSel = elegido;
                chipNivel.setText(elegido != null ? elegido : "Nivel");
                cargarExamenes();
            });
        });

        chipCreador.setOnClickListener(v -> {
            // creador_array = Todos, Estudiante, Profesor (ya existe en strings.xml)
            List<String> opciones = new ArrayList<>(
                    Arrays.asList(getResources().getStringArray(R.array.creador_array)));
            opciones.remove(0); // sacamos "Todos": el menú agrega su propia opción
            mostrarMenu(v, "Todos", opciones, elegido -> {
                // Django espera 'profesor' o 'estudiante' en minúscula
                creadorSel = elegido != null ? elegido.toLowerCase() : null;
                chipCreador.setText(elegido != null ? elegido : "Creador");
                cargarExamenes();
            });
        });

        chipLimpiar.setOnClickListener(v -> {
            categoriaSel = null;
            nivelSel = null;
            creadorSel = null;
            chipCategoria.setText("Categoría");
            chipNivel.setText("Nivel");
            chipCreador.setText("Creador");
            cargarExamenes();
        });
    }

    private void mostrarMenu(View anchor, String textoTodos, List<String> opciones,
                             Consumer<String> alElegir) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenu().add(0, 0, 0, textoTodos);
        for (int i = 0; i < opciones.size(); i++) {
            popup.getMenu().add(0, i + 1, i + 1, opciones.get(i));
        }
        popup.setOnMenuItemClickListener(item -> {
            alElegir.accept(item.getItemId() == 0 ? null : item.getTitle().toString());
            return true;
        });
        popup.show();
    }

    private void cargarOpcionesFiltros() {
        ApiClient.getApiService().getCategorias().enqueue(new Callback<List<FiltroItem>>() {
            @Override
            public void onResponse(Call<List<FiltroItem>> call, Response<List<FiltroItem>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    categorias.clear();
                    for (FiltroItem f : r.body()) categorias.add(f.getNombre());
                }
            }

            @Override
            public void onFailure(Call<List<FiltroItem>> call, Throwable t) { }
        });

        ApiClient.getApiService().getNiveles().enqueue(new Callback<List<FiltroItem>>() {
            @Override
            public void onResponse(Call<List<FiltroItem>> call, Response<List<FiltroItem>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    niveles.clear();
                    for (FiltroItem f : r.body()) niveles.add(f.getNombre());
                }
            }

            @Override
            public void onFailure(Call<List<FiltroItem>> call, Throwable t) { }
        });
    }

    private void cargarExamenes() {
        if (llamadaActual != null) llamadaActual.cancel();

        progress.setVisibility(View.VISIBLE);
        tvVacio.setVisibility(View.GONE);

        llamadaActual = ApiClient.getApiService()
                .getExamenesCatalogo (categoriaSel, nivelSel, creadorSel, null);

        llamadaActual.enqueue(new Callback<List<ExamenCatalogo>>() {
            @Override
            public void onResponse(Call<List<ExamenCatalogo>> call,
                                   Response<List<ExamenCatalogo>> r) {
                progress.setVisibility(View.GONE);
                if (r.isSuccessful() && r.body() != null) {
                    adapter.setItems(r.body());
                    tvVacio.setVisibility(r.body().isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    Toast.makeText(CatalogoActivity.this,
                            "Error al cargar exámenes (código " + r.code() + ")",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ExamenCatalogo>> call, Throwable t) {
                if (call.isCanceled()) return; // la cancelamos nosotros al cambiar de filtro
                progress.setVisibility(View.GONE);
                Toast.makeText(CatalogoActivity.this,
                        "No se pudo conectar con el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void abrirDetalle(ExamenCatalogo examen) {
        Intent intent = new Intent(this, com.ispc.testace.ui.examenes.VistaPreviaExamenes.class);
        intent.putExtra("EXAMEN_ID", String.valueOf(examen.getId()));
        intent.putExtra("EXAMEN_SLUG", examen.getSlug());
        intent.putExtra("EXAMEN_NOMBRE", examen.getTitulo());
        intent.putExtra("EXAMEN_DESCRIPCION", examen.getDescripcion());
        intent.putExtra("EXAMEN_TIEMPO", examen.getTiempoLimite());
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