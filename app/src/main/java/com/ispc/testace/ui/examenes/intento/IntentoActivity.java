package com.ispc.testace.ui.examenes.intento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ispc.testace.R;
import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.ExamenEjemplo;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.Pregunta;

import java.util.List;

public class IntentoActivity extends AppCompatActivity {
    private Examen examen;
    private int indicePreguntaActual = 0;

    private List<Pregunta> preguntas;
    private RadioGroup rgOpciones;
    private TextView tvPreguntaActual;
    private Button btnSiguiente;
    private Button btnAnterior;
    private TextView tvExamenFinalizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intento);


        examen = ExamenEjemplo.crearExamenHarcodeado();
        preguntas = examen.getPreguntas();


        tvPreguntaActual = findViewById(R.id.tvPreguntaActual);
        rgOpciones = findViewById(R.id.rgOpciones);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnAnterior= findViewById(R.id.btnAnterior);
        tvExamenFinalizado=findViewById(R.id.tvExamenFinalizado);


        mostrarPreguntaActual();


        btnSiguiente.setOnClickListener(v -> {
            if (indicePreguntaActual < preguntas.size() - 1) {

                    indicePreguntaActual++;
                    mostrarPreguntaActual();

            } else {
                tvExamenFinalizado.setVisibility(View.VISIBLE);
                tvPreguntaActual.setVisibility(View.GONE);
                rgOpciones.setVisibility(View.GONE);
                btnSiguiente.setVisibility(View.GONE);
                btnAnterior.setVisibility(View.GONE);
            }
        });

        btnAnterior.setOnClickListener(v -> {
            if (indicePreguntaActual > 0) {
                indicePreguntaActual--;
                mostrarPreguntaActual();
            }
        });
    }

    private void mostrarPreguntaActual() {
        Pregunta pregunta = preguntas.get(indicePreguntaActual);
        tvPreguntaActual.setText(pregunta.getEnunciado());


        rgOpciones.removeAllViews();


        if (pregunta.getTipo().equals("opcion_multiple")) {
            for (Opcion opcion : pregunta.getOpciones()) {
                RadioButton rb = new RadioButton(this);
                rb.setText(opcion.getTextoOpcion());

                rgOpciones.addView(rb);
            }
        } else if (pregunta.getTipo().equals("verdadero_falso")) {
            RadioButton rbVerdadero = new RadioButton(this);
            rbVerdadero.setText("Verdadero");
            RadioButton rbFalso = new RadioButton(this);
            rbFalso.setText("Falso");
            rgOpciones.addView(rbVerdadero);
            rgOpciones.addView(rbFalso);
        }
    }



}