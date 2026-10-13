package com.ispc.testace.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.ispc.testace.R;
import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.data.model.ExamenCatalogo;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.model.Perfil;
import com.ispc.testace.data.service.ApiService;
import com.ispc.testace.ui.examenes.modificar.ModificarExamenActivity;
import com.ispc.testace.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ApiService api;

    private LinearLayout layoutPerfil;
    private LinearLayout layoutExamenes;
    private LinearLayout contenedorMisExamenes;
    private LinearLayout contenedorIntentos;

    private EditText etEmail;
    private EditText etNombre;
    private EditText etApellido;
    private Button btnGuardar;
    private Button btnEliminarCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // El perfil necesita sesión: sin token, se manda al login.
        if (!TokenManager.estaLogueado()) {
            Toast.makeText(this, "Iniciá sesión para ver tu perfil", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);
        api = ApiClient.getApiService();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        layoutPerfil = findViewById(R.id.layoutPerfil);
        layoutExamenes = findViewById(R.id.layoutExamenes);
        contenedorMisExamenes = findViewById(R.id.contenedorMisExamenes);
        contenedorIntentos = findViewById(R.id.contenedorIntentos);
        etEmail = findViewById(R.id.etEmail);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);

        setupTabs();
        findViewById(R.id.btnEditNombre).setOnClickListener(v -> enableEditing(etNombre));
        findViewById(R.id.btnEditApellido).setOnClickListener(v -> enableEditing(etApellido));
        btnGuardar.setOnClickListener(v -> guardarPerfil());
        btnEliminarCuenta.setOnClickListener(v -> confirmarEliminarCuenta());

        cargarPerfil();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Al volver de "Modificar examen" se refresca la lista.
        if (api != null) cargarMisExamenes();
    }

    // ------------------------------------------------------------- Tabs

    private void setupTabs() {
        TabLayout tabLayout = findViewById(R.id.tabLayoutProfile);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                boolean perfil = tab.getPosition() == 0;
                layoutPerfil.setVisibility(perfil ? View.VISIBLE : View.GONE);
                layoutExamenes.setVisibility(perfil ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    // ----------------------------------------------------- Perfil (CRUD)

    /** READ: GET auth/perfil/ */
    private void cargarPerfil() {
        api.getPerfil().enqueue(new Callback<Perfil>() {
            @Override
            public void onResponse(Call<Perfil> call, Response<Perfil> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    avisarError("No se pudo cargar el perfil", response.code());
                    return;
                }
                Perfil p = response.body();
                etEmail.setText(p.getEmail());
                etNombre.setText(p.getFirstName());
                etApellido.setText(p.getLastName());
                mostrarIntentos(p.getIntentos());
            }

            @Override
            public void onFailure(Call<Perfil> call, Throwable t) {
                avisarFallaRed(t);
            }
        });
    }

    /** UPDATE: PATCH auth/perfil/ (solo nombre y apellido) */
    private void guardarPerfil() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            Toast.makeText(this, "Nombre y apellido no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> datos = new HashMap<>();
        datos.put("first_name", nombre);
        datos.put("last_name", apellido);

        btnGuardar.setEnabled(false);
        api.updatePerfil(datos).enqueue(new Callback<Perfil>() {
            @Override
            public void onResponse(Call<Perfil> call, Response<Perfil> response) {
                btnGuardar.setEnabled(true);
                if (response.isSuccessful()) {
                    etNombre.setEnabled(false);
                    etApellido.setEnabled(false);
                    Toast.makeText(ProfileActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    avisarError("No se pudo guardar", response.code());
                }
            }

            @Override
            public void onFailure(Call<Perfil> call, Throwable t) {
                btnGuardar.setEnabled(true);
                avisarFallaRed(t);
            }
        });
    }

    /** DELETE: DELETE auth/perfil/ (con confirmación) */
    private void confirmarEliminarCuenta() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar cuenta")
                .setMessage("Esta acción no se puede deshacer. ¿Querés eliminar tu cuenta?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar", (d, w) -> eliminarCuenta())
                .show();
    }

    private void eliminarCuenta() {
        btnEliminarCuenta.setEnabled(false);
        api.deletePerfil().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    TokenManager.cerrarSesion();
                    Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    btnEliminarCuenta.setEnabled(true);
                    avisarError("No se pudo eliminar la cuenta", response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnEliminarCuenta.setEnabled(true);
                avisarFallaRed(t);
            }
        });
    }

    private void enableEditing(EditText editText) {
        editText.setEnabled(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
    }

    // -------------------------------------------------- Panel de examen

    /** Exámenes recientes = intentos que ya vienen dentro de auth/perfil/ */
    private void mostrarIntentos(List<IntentoExamen> intentos) {
        contenedorIntentos.removeAllViews();
        if (intentos == null || intentos.isEmpty()) {
            contenedorIntentos.addView(textoVacio("Todavía no realizaste ningún examen."));
            return;
        }
        for (IntentoExamen intento : intentos) {
            LinearLayout fila = fila();
            fila.addView(texto(intento.getExamen_titulo(), 1f));
            Double r = intento.getResultado();
            fila.addView(texto(r == null ? "En curso" : String.format(Locale.getDefault(), "%.1f", r), 0f));
            contenedorIntentos.addView(fila);
        }
    }

    /** Exámenes creados por el usuario: GET examenes/?mine=true */
    private void cargarMisExamenes() {
        api.getMisExamenes(true).enqueue(new Callback<List<ExamenCatalogo>>() {
            @Override
            public void onResponse(Call<List<ExamenCatalogo>> call, Response<List<ExamenCatalogo>> response) {
                contenedorMisExamenes.removeAllViews();
                if (!response.isSuccessful() || response.body() == null) {
                    avisarError("No se pudieron cargar tus exámenes", response.code());
                    return;
                }
                if (response.body().isEmpty()) {
                    contenedorMisExamenes.addView(textoVacio("Todavía no creaste ningún examen."));
                    return;
                }
                for (ExamenCatalogo examen : response.body()) {
                    contenedorMisExamenes.addView(filaMiExamen(examen));
                }
            }

            @Override
            public void onFailure(Call<List<ExamenCatalogo>> call, Throwable t) {
                avisarFallaRed(t);
            }
        });
    }

    private View filaMiExamen(ExamenCatalogo examen) {
        LinearLayout fila = fila();
        fila.addView(texto(examen.getTitulo(), 1f));

        Button editar = new Button(this);
        editar.setText("Editar");
        editar.setOnClickListener(v -> {
            Intent i = new Intent(this, ModificarExamenActivity.class);
            i.putExtra("examen_slug", examen.getSlug());
            startActivity(i);
        });

        Button eliminar = new Button(this);
        eliminar.setText("Eliminar");
        eliminar.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Eliminar examen")
                .setMessage("¿Eliminar \"" + examen.getTitulo() + "\"?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Eliminar", (d, w) -> eliminarExamen(examen.getSlug()))
                .show());

        fila.addView(editar);
        fila.addView(eliminar);
        return fila;
    }

    private void eliminarExamen(String slug) {
        api.deleteExamen(slug).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Examen eliminado", Toast.LENGTH_SHORT).show();
                    cargarMisExamenes();
                } else {
                    avisarError("No se pudo eliminar el examen", response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                avisarFallaRed(t);
            }
        });
    }

    // ---------------------------------------------------------- Helpers

    private LinearLayout fila() {
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(0, dp(8), 0, dp(8));
        return fila;
    }

    private TextView texto(String contenido, float peso) {
        TextView tv = new TextView(this);
        tv.setText(contenido == null ? "" : contenido);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                peso > 0 ? 0 : LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, peso));
        return tv;
    }

    private TextView textoVacio(String mensaje) {
        TextView tv = texto(mensaje, 0f);
        tv.setPadding(0, dp(8), 0, dp(8));
        return tv;
    }

    private int dp(int valor) {
        return Math.round(valor * getResources().getDisplayMetrics().density);
    }

    private void avisarError(String mensaje, int codigo) {
        Toast.makeText(this, mensaje + " (código " + codigo + ")", Toast.LENGTH_LONG).show();
    }

    private void avisarFallaRed(Throwable t) {
        Toast.makeText(this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
