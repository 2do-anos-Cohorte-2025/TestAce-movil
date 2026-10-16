
package com.ispc.testace.ui.examenes;


import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ispc.testace.R;
import com.ispc.testace.data.ApiClient;
import com.ispc.testace.data.service.ApiService;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.model.Opcion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearPreguntasActivity extends AppCompatActivity {

    private EditText edtEnunciadoPregunta;
    private LinearLayout contenedorOpciones;
    private CheckBox chkVariasRespuestas;
    private Button btnAgregarOpcion;
    private Button btnCrearPregunta;

    private Button btnVolver;
    private Button btnPreguntaAnterior;
    private Button btnFinalizarExamen;

    private ApiService apiService;
    private int examenId;

    private int preguntaActualId = -1;
    private List<Pregunta> preguntasGuardadas = new ArrayList<>();

    private final List<OpcionFila> filasOpciones = new ArrayList<>();

    private static class OpcionFila {
        EditText edtTexto;
        CheckBox chkCorrecta;

        OpcionFila(EditText edtTexto, CheckBox chkCorrecta) {
            this.edtTexto = edtTexto;
            this.chkCorrecta = chkCorrecta;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_preguntas);

        edtEnunciadoPregunta = findViewById(R.id.edtEnunciadoPregunta);
        contenedorOpciones = findViewById(R.id.contenedorOpciones);
        chkVariasRespuestas = findViewById(R.id.chkVariasRespuestas);
        btnAgregarOpcion = findViewById(R.id.btnAgregarOpcion);
        btnCrearPregunta = findViewById(R.id.btnCrearPregunta);
        btnVolver = findViewById(R.id.btnVolver);
        btnPreguntaAnterior = findViewById(R.id.btnPreguntaAnterior);
        btnFinalizarExamen = findViewById(R.id.btnFinalizarExamen);

        apiService = ApiClient.getApiService();

        examenId = getIntent().getIntExtra("EXAMEN_ID", -1);

        if (examenId == -1) {
            Toast.makeText(this,
                    "No se recibió el ID del examen",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        agregarFilaOpcion();
        agregarFilaOpcion();

        btnAgregarOpcion.setOnClickListener(v -> agregarFilaOpcion());

        chkVariasRespuestas.setOnCheckedChangeListener((button, marcado) -> {
            if (!marcado) {
                boolean yaHayUnaCorrecta = false;

                for (OpcionFila fila : filasOpciones) {
                    if (fila.chkCorrecta.isChecked()) {
                        if (!yaHayUnaCorrecta) {
                            yaHayUnaCorrecta = true;
                        } else {
                            fila.chkCorrecta.setChecked(false);
                        }
                    }
                }
            }
        });



        btnCrearPregunta.setOnClickListener(v -> crearPregunta());
        btnPreguntaAnterior.setOnClickListener(v -> cargarPreguntaAnterior());

        btnVolver.setOnClickListener(v -> finish());

        btnFinalizarExamen.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Finalizar examen")
                    .setMessage("¿Terminaste de cargar todas las preguntas?")
                    .setNegativeButton("Seguir editando", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Finalizar", (dialog, which) -> finish())
                    .show();
        });
    }



    private void cargarPreguntaAnterior() {
        btnPreguntaAnterior.setEnabled(false);

        apiService.getPreguntasByExamen(examenId).enqueue(
                new Callback<List<Pregunta>>() {
                    @Override
                    public void onResponse(
                            Call<List<Pregunta>> call,
                            Response<List<Pregunta>> response) {

                        btnPreguntaAnterior.setEnabled(true);

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(CrearPreguntasActivity.this,
                                    "No se pudieron cargar las preguntas",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        preguntasGuardadas = response.body();

                        if (preguntasGuardadas.isEmpty()) {
                            Toast.makeText(CrearPreguntasActivity.this,
                                    "Todavía no hay preguntas guardadas",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int indice = preguntasGuardadas.size() - 1;

                        if (preguntaActualId != -1) {
                            for (int i = 0; i < preguntasGuardadas.size(); i++) {
                                if (preguntasGuardadas.get(i).getId() == preguntaActualId) {
                                    indice = i - 1;
                                    break;
                                }
                            }
                        }

                        if (indice < 0) {
                            Toast.makeText(CrearPreguntasActivity.this,
                                    "Esta es la primera pregunta",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Pregunta anterior = preguntasGuardadas.get(indice);
                        cargarDatosPregunta(anterior);
                    }

                    @Override
                    public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                        btnPreguntaAnterior.setEnabled(true);
                        Toast.makeText(CrearPreguntasActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void cargarDatosPregunta(Pregunta pregunta) {
        preguntaActualId = pregunta.getId();
        edtEnunciadoPregunta.setText(pregunta.getEnunciado());

        contenedorOpciones.removeAllViews();
        filasOpciones.clear();

        apiService.getOpcionesByPregunta(pregunta.getId()).enqueue(
                new Callback<List<Opcion>>() {
                    @Override
                    public void onResponse(
                            Call<List<Opcion>> call,
                            Response<List<Opcion>> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(CrearPreguntasActivity.this,
                                    "No se pudieron cargar las opciones",
                                    Toast.LENGTH_SHORT).show();
                            agregarFilaOpcion();
                            agregarFilaOpcion();
                            return;
                        }

                        List<Opcion> opciones = response.body();

                        for (Opcion opcion : opciones) {
                            agregarFilaOpcion();

                            OpcionFila fila = filasOpciones.get(filasOpciones.size() - 1);
                            fila.edtTexto.setText(opcion.getTextoOpcion());
                            fila.chkCorrecta.setChecked(opcion.isEsCorrecta());
                        }

                        while (filasOpciones.size() < 2) {
                            agregarFilaOpcion();
                        }

                        Toast.makeText(CrearPreguntasActivity.this,
                                "Pregunta cargada para revisar",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<List<Opcion>> call, Throwable t) {
                        Toast.makeText(CrearPreguntasActivity.this,
                                "Error al cargar las opciones: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void agregarFilaOpcion() {
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(android.view.Gravity.CENTER_VERTICAL);

        EditText edtOpcion = new EditText(this);
        edtOpcion.setHint("Texto de la opción");
        edtOpcion.setSingleLine(false);
        edtOpcion.setMinHeight(48);

        LinearLayout.LayoutParams parametrosTexto =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1
                );

        fila.addView(edtOpcion, parametrosTexto);

        CheckBox chkCorrecta = new CheckBox(this);
        chkCorrecta.setText("Es correcta");

        chkCorrecta.setOnCheckedChangeListener((button, marcado) -> {
            if (marcado && !chkVariasRespuestas.isChecked()) {
                for (OpcionFila otra : filasOpciones) {
                    if (otra.chkCorrecta != chkCorrecta) {
                        otra.chkCorrecta.setChecked(false);
                    }
                }
            }
        });

        fila.addView(chkCorrecta);

        LinearLayout.LayoutParams parametrosFila =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        parametrosFila.bottomMargin = 8;

        contenedorOpciones.addView(fila, parametrosFila);
        filasOpciones.add(new OpcionFila(edtOpcion, chkCorrecta));
    }

    private void crearPregunta() {
        String enunciado =
                edtEnunciadoPregunta.getText().toString().trim();

        if (enunciado.isEmpty()) {
            Toast.makeText(this,
                    "Escribí el enunciado de la pregunta",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        List<OpcionFila> opcionesValidas = new ArrayList<>();
        boolean hayCorrecta = false;

        for (OpcionFila fila : filasOpciones) {
            String texto = fila.edtTexto.getText().toString().trim();

            if (!texto.isEmpty()) {
                opcionesValidas.add(fila);

                if (fila.chkCorrecta.isChecked()) {
                    hayCorrecta = true;
                }
            }
        }

        if (opcionesValidas.size() < 2) {
            Toast.makeText(this,
                    "Ingresá al menos dos opciones",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hayCorrecta) {
            Toast.makeText(this,
                    "Marcá al menos una opción correcta",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Pregunta pregunta = new Pregunta();
        pregunta.setExamen(examenId);
        pregunta.setEnunciado(enunciado);
        pregunta.setTipo("opcion_multiple");
        pregunta.setPermitir_varias_respuestas(chkVariasRespuestas.isChecked());
        pregunta.setPuntos(1.0);

        btnCrearPregunta.setEnabled(false);

        apiService.createPregunta(pregunta).enqueue(new Callback<Pregunta>() {
            @Override
            public void onResponse(
                    Call<Pregunta> call,
                    Response<Pregunta> response) {

                if (response.isSuccessful() && response.body() != null) {
                    int preguntaId = response.body().getId();

                    guardarOpcion(preguntaId, 0, opcionesValidas);
                } else {
                    btnCrearPregunta.setEnabled(true);

                    Toast.makeText(CrearPreguntasActivity.this,
                            "No se pudo crear la pregunta. Código: "
                                    + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Pregunta> call, Throwable t) {
                btnCrearPregunta.setEnabled(true);

                Toast.makeText(CrearPreguntasActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void guardarOpcion(
            int preguntaId,
            int indice,
            List<OpcionFila> opciones) {

        if (indice >= opciones.size()) {
            btnCrearPregunta.setEnabled(true);

            Toast.makeText(this,
                    "Pregunta y opciones guardadas correctamente",
                    Toast.LENGTH_LONG).show();

            limpiarFormulario();
            return;
        }

        OpcionFila fila = opciones.get(indice);

        Map<String, Object> datos = new HashMap<>();
        datos.put("pregunta", preguntaId);
        datos.put("texto_opcion",
                fila.edtTexto.getText().toString().trim());
        datos.put("es_correcta",
                fila.chkCorrecta.isChecked());

        apiService.createOpcion(datos).enqueue(new Callback<Opcion>() {
            @Override
            public void onResponse(
                    Call<Opcion> call,
                    Response<Opcion> response) {

                if (response.isSuccessful()) {
                    guardarOpcion(preguntaId, indice + 1, opciones);
                } else {
                    btnCrearPregunta.setEnabled(true);

                    Toast.makeText(CrearPreguntasActivity.this,
                            "La pregunta se creó, pero no se pudo guardar "
                                    + "una opción. Código: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Opcion> call, Throwable t) {
                btnCrearPregunta.setEnabled(true);

                Toast.makeText(CrearPreguntasActivity.this,
                        "Error al guardar una opción: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void limpiarFormulario() {
        edtEnunciadoPregunta.setText("");
        contenedorOpciones.removeAllViews();
        filasOpciones.clear();
        chkVariasRespuestas.setChecked(false);

        agregarFilaOpcion();
        agregarFilaOpcion();
    }
}
