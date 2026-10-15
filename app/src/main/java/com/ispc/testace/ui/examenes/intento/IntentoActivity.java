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
import com.ispc.testace.ui.examenes.intento.adapter.IntentoAdapter;
import com.ispc.testace.ui.examenes.intento.adapter.OpcionAdapter;

import java.util.ArrayList;
import java.util.List;

public class IntentoActivity extends AppCompatActivity {
    private IntentoViewModel viewModel;
    private TextView textViewNombreExamen, textViewTiempoRestante, textViewPreguntaActual;
    private ImageView imageViewPregunta;
    private EditText editTextRespuestaTexto;
    private Button buttonAnterior, buttonSiguiente, buttonFinalizar, buttonCancelar;
    private IntentoAdapter intentoAdapter;
    /*private OpcionAdapter opcionAdapter;*/
    private RecyclerView recyclerViewPreguntas;

    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intento);

        viewModel = new ViewModelProvider(this).get(IntentoViewModel.class);


        textViewNombreExamen = findViewById(R.id.textViewNombreExamen);
        textViewTiempoRestante = findViewById(R.id.textViewTiempoRestante);
        buttonAnterior = findViewById(R.id.buttonAnterior);
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        buttonFinalizar = findViewById(R.id.buttonFinalizar);
        buttonCancelar = findViewById(R.id.buttonCancelar);
        recyclerViewPreguntas = findViewById(R.id.recyclerViewPreguntas);



        if (recyclerViewPreguntas == null) {
            Log.e("IntentoActivity", "Error: recyclerViewPreguntas no encontrado en el layout");
            Toast.makeText(this, "Error: RecyclerView no encontrado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Configurar RecyclerView para las preguntas y opciones
        recyclerViewPreguntas.setLayoutManager(new LinearLayoutManager(this));
        intentoAdapter = new IntentoAdapter(viewModel);
        recyclerViewPreguntas.setAdapter(intentoAdapter);

        viewModel.getPreguntasLiveData().observe(this, preguntas -> {
            if (preguntas != null) {
                intentoAdapter.setPreguntas(preguntas);
                actualizarPreguntaActual(0); // Actualizar con el índice inicial
            }
        });

        viewModel.getPreguntaActualIndex().observe(this, index -> {
            actualizarPreguntaActual(index);
        });

        viewModel.getPreguntaActualIndex().observe(this, this::actualizarPreguntaActual);


        int duracionExamen = getIntent().getIntExtra("duracion_examen", 30); // 30 minutos por defecto
        viewModel.iniciarTiempoExamen(duracionExamen);
        iniciarContadorTiempo();

        // Para observar los cambios en el viewModel
        viewModel.getIntentoExamenLiveData().observe(this, intentoExamen -> {
            if (intentoExamen != null) {
                textViewNombreExamen.setText("Examen: " + intentoExamen.getExamen_titulo());
            }
        });

        viewModel.getPreguntaActualIndex().observe(this, index -> {
            actualizarEstadoBotones();
        });


        viewModel.getOpcionesLiveData().observe(this, opciones -> {
            if (opciones != null) {
                intentoAdapter.setOpciones(opciones);
            }
        });
        viewModel.getPreguntaActualIndex().observe(this, index ->{
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

        viewModel.getResultadoLiveData().observe(this, resultado -> {
            if(viewModel.getIntentoExamenLiveData().getValue().getFechaFin()!=null){
                viewModel.getTiempoTranscurridoLiveData().observe(this, tiempo -> {
                    if(tiempo != 0) {
                        mostrarResultado(resultado, tiempo.floatValue());
                    }
                });}
        });

        //Botones listener
        buttonAnterior.setOnClickListener(v -> {
                viewModel.preguntaAnterior();
        });

        buttonSiguiente.setOnClickListener(v -> {
            Integer currentIndex = viewModel.getPreguntaActualIndex().getValue();
            List<Pregunta> preguntas = viewModel.getPreguntasLiveData().getValue();


            if (currentIndex == null || preguntas == null || preguntas.isEmpty()) {
                return;
            }

            if (!viewModel.hayRespuesta(preguntas.get(currentIndex).getId(), preguntas.get(currentIndex).getTipo())) {

                Toast.makeText(this, "Debés responder la pregunta antes de continuar", Toast.LENGTH_SHORT).show();
                return;
            }
                viewModel.siguientePregunta();

        });

        buttonFinalizar.setOnClickListener(v -> {
            Integer currentIndex = viewModel.getPreguntaActualIndex().getValue();
            List<Pregunta> preguntas = viewModel.getPreguntasLiveData().getValue();
            if (!viewModel.hayRespuesta(preguntas.get(currentIndex).getId(), preguntas.get(currentIndex).getTipo())) {

                Toast.makeText(this, "Debés responder la pregunta antes de continuar", Toast.LENGTH_SHORT).show();
                return;
            }
            mostrarDialogoFinalizar();
        });

        buttonCancelar.setOnClickListener(v -> {
            mostrarDialogoCancelar();
        });


        //Obteniendo los intent para inicializar el examen
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



    private void actualizarPreguntaActual(Integer currentIndex) {
        List<Pregunta> preguntas = viewModel.getPreguntasLiveData().getValue();

        if (currentIndex != null && preguntas != null && currentIndex < preguntas.size()) {
            intentoAdapter.setCurrentPosition(currentIndex);
            actualizarEstadoBotones();
        }
    }


    private void actualizarEstadoBotones() {
        Integer currentIndex = viewModel.getPreguntaActualIndex().getValue();
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


    private void mostrarResultado(Double resultado, float tiempoTranscurrido) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Examen Finalizado");
        builder.setMessage(
                "Resultado: " + resultado + " puntos\n" +
                        "Tiempo transcurrido: " + tiempoTranscurrido + " minutos"
        );
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            dialog.dismiss();
            finish(); // Cerrar la actividad
        });
        builder.setCancelable(false); // Evitar que se cierre al tocar fuera
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}