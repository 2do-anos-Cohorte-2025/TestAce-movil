package com.ispc.testace.ui.examenes.intento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ispc.testace.R;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.ui.examenes.intento.adapter.OpcionAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntentoActivity extends AppCompatActivity {
    private IntentoViewModel viewModel;
    private TextView textViewNombreExamen, textViewTiempoRestante, textViewPreguntaActual;
    private ImageView imageViewPregunta;
    private LinearLayout containerOpciones;
    private EditText editTextRespuestaTexto;
    private Button buttonAnterior, buttonSiguiente, buttonFinalizar, buttonCancelar;
    private OpcionAdapter opcionAdapter;
    private RecyclerView recyclerViewOpciones;

    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intento);

        viewModel = new ViewModelProvider(this).get(IntentoViewModel.class);


        textViewNombreExamen = findViewById(R.id.textViewNombreExamen);
        textViewTiempoRestante = findViewById(R.id.textViewTiempoRestante);
        textViewPreguntaActual = findViewById(R.id.textViewPreguntaActual);
        imageViewPregunta = findViewById(R.id.imageViewPregunta);
        /*containerOpciones = findViewById(R.id.containerOpciones);*/
        editTextRespuestaTexto = findViewById(R.id.editTextRespuestaTexto);
        buttonAnterior = findViewById(R.id.buttonAnterior);
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        buttonFinalizar = findViewById(R.id.buttonFinalizar);
        buttonCancelar = findViewById(R.id.buttonCancelar);


        recyclerViewOpciones = findViewById(R.id.recyclerViewOpciones);
        if (recyclerViewOpciones == null) {
            Log.e("IntentoActivity", "RecyclerView no encontrado en el layout");
        } else {
            recyclerViewOpciones.setLayoutManager(new LinearLayoutManager(this));
            opcionAdapter = new OpcionAdapter(new ArrayList<>(), this::onOpcionSelected);
            recyclerViewOpciones.setAdapter(opcionAdapter);
        }

        int duracionExamen = getIntent().getIntExtra("duracion_examen", 30); // 30 minutos por defecto
        viewModel.iniciarTiempoExamen(duracionExamen);
        iniciarContadorTiempo();

        // Para observar los cambios en el viewModel
        viewModel.getIntentoExamenLiveData().observe(this, intentoExamen -> {
            if (intentoExamen != null) {
                textViewNombreExamen.setText("Examen: " + intentoExamen.getExamen_titulo());
            }
        });

        viewModel.getPreguntaIndex().observe(this, index -> {
            actualizarEstadoBotones();
        });

        viewModel.getPreguntaActualLiveData().observe(this, pregunta -> {
            if (pregunta != null) {
                textViewPreguntaActual.setText(pregunta.getEnunciado());
                if (pregunta.getImagen_pregunta() != null) {
                    imageViewPregunta.setVisibility(View.VISIBLE);
                    Glide.with(this)
                            .load(pregunta.getImagen_pregunta())
                            .placeholder(R.drawable.placeholder_imagen)
                            .into(imageViewPregunta);
                } else {
                    imageViewPregunta.setVisibility(View.GONE);
                }
                actualizarEstadoBotones();
            }
        });

        viewModel.getOpcionesLiveData().observe(this, opciones -> {
            if (opciones != null) {
                opcionAdapter.updateOpciones(opciones);
            }
        });
        viewModel.getPreguntaIndex().observe(this, index ->{
            buttonAnterior.setEnabled(index > 0);
            buttonSiguiente.setEnabled(index < viewModel.getPreguntasLiveData().getValue().size()-1);

        });
        /*viewModel.isLoading().observe(this. isLoading ->{
            Barra de progreso, averiguar como agregar
        });*/
        viewModel.getErrorMessage().observe(this, error->{
            if(error != null){
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        buttonAnterior.setOnClickListener(v -> {
                viewModel.preguntaAnterior();
        });

        buttonSiguiente.setOnClickListener(v -> {
            Integer currentIndex = viewModel.getPreguntaIndex().getValue();
            List<Pregunta> preguntas = viewModel.getPreguntasLiveData().getValue();

            if (currentIndex == null || preguntas == null || preguntas.isEmpty()) {
                return;
            }


            if (!viewModel.hayOpcionSeleccionada(preguntas.get(currentIndex).getId())) {
                Toast.makeText(this, "Debe seleccionar una opción antes de continuar", Toast.LENGTH_SHORT).show();
                return;
            }


                viewModel.siguientePregunta();

        });

        buttonFinalizar.setOnClickListener(v -> {
            mostrarDialogoFinalizar();
        });

        buttonCancelar.setOnClickListener(v -> {
            mostrarDialogoCancelar();
        });



        int examenId = getIntent().getIntExtra("examen_id", -1);
        int usuarioId = getIntent().getIntExtra("usuario_id", -1);


        if (examenId != -1) {
            viewModel.iniciarIntentoExamen(examenId, usuarioId);
        } else {
            Toast.makeText(this, "Error: ID de examen no valido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void iniciarContadorTiempo() {
        countDownTimer = new CountDownTimer(viewModel.getTiempoRestanteEnMilis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                viewModel.tiempoRestanteEnMilis = millisUntilFinished;
                actualizarTiempoRestante();
            }

            @Override
            public void onFinish() {
                // Tiempo agotado, finalizar examen automaticamente
                viewModel.finalizarExamen();
                finish();
            }
        }.start();
    }


    private void actualizarTiempoRestante() {
        int minutos = (int) (viewModel.getTiempoRestanteEnMilis() / 1000) / 60;
        int segundos = (int) (viewModel.getTiempoRestanteEnMilis() / 1000) % 60;
        textViewTiempoRestante.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void actualizarEstadoBotones() {
        Integer currentIndex = viewModel.getPreguntaIndex().getValue();
        List<Pregunta> preguntas = viewModel.getPreguntasLiveData().getValue();

        if (currentIndex == null || preguntas == null || preguntas.isEmpty()) {
            buttonAnterior.setEnabled(false);
            buttonSiguiente.setEnabled(false);
            buttonFinalizar.setVisibility(View.GONE);
            return;
        }

        buttonAnterior.setEnabled(currentIndex > 0);

        // Se muestra el boton Finalizar solo en la ultima pregunta
        if (currentIndex == preguntas.size() - 1) {
            buttonFinalizar.setVisibility(View.VISIBLE);
        } else {
            buttonFinalizar.setVisibility(View.GONE);
        }
    }

    private void mostrarDialogoFinalizar() {
        new AlertDialog.Builder(this)
                .setTitle("Finalizar examen")
                .setMessage("No podrás volver atrás una vez finalizado el examen ¿Deseas finalizar?")
                .setPositiveButton("Finalizar", (dialog, which) -> {
                    viewModel.finalizarExamen();

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoCancelar() {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar examen")
                .setMessage("Se borrará tu intento ¿Deseas cancelar?")
                .setPositiveButton("Cancelar", (dialog, which) -> {
                    viewModel.cancelarExamen();
                    finish();
                })
                .setNegativeButton("Continuar", null)
                .show();
    }

    private void onOpcionSelected(int opcionId) {
        Pregunta preguntaActual = viewModel.getPreguntaActualLiveData().getValue();
        if (preguntaActual != null) {
            List<Opcion> opciones = viewModel.getOpcionesLiveData().getValue();
            if (opciones != null) {
                for (Opcion opcion : opciones) {
                    opcion.setSelected(false);
                }
            }


            viewModel.guardarRespuesta(preguntaActual.getId(), opcionId,null);
            viewModel.guardarSeleccionOpcion(preguntaActual.getId(), opcionId);

            opcionAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}