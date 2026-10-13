package com.ispc.testace.ui.examenes.modificar;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.ispc.testace.R;
import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.Pregunta;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class ModificarExamenActivity extends AppCompatActivity {
    private ModificarExamenViewModel viewModel;

    private LinearLayout contenedorFormulario;
    private ProgressBar progressCarga;
    private EditText campoTitulo;
    private EditText campoDescripcion;
    private String examenSlug;
    private boolean formularioCreado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_examen);


        contenedorFormulario = findViewById(R.id.contenedorFormulario);
        progressCarga = findViewById(R.id.progressCarga);


        Button botonVolver = findViewById(R.id.botonVolver);
        botonVolver.setOnClickListener(v -> finish());


        // Conseguir el slug del examen
        examenSlug = getIntent().getStringExtra("examen_slug");

        if (examenSlug == null || examenSlug.trim().isEmpty()) {
            Toast.makeText(
                    this,
                    "No se recibió el examen seleccionado.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }


        viewModel = new ViewModelProvider(this).get(ModificarExamenViewModel.class);


        observarDatos();viewModel.cargarExamen(examenSlug);


    }





    private void observarDatos() {

        viewModel.getCargando().observe(this, cargando -> {
            progressCarga.setVisibility(Boolean.TRUE.equals(cargando)
                    ? View.VISIBLE
                    : View.GONE
            );

            // Renderizar despues de cargar
            if (Boolean.FALSE.equals(cargando) && viewModel.getExamen().getValue() != null && !formularioCreado) {
                formularioCreado = true;
                mostrarFormulario();
            }
        });

        viewModel.getMensaje().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(
                        this,
                        mensaje,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void mostrarFormulario() {

        Examen examen = viewModel.getExamen().getValue();

        List<Pregunta> preguntas =
                viewModel.getPreguntas().getValue();

        if (examen == null) {
            return;
        }

        contenedorFormulario.removeAllViews();

        agregarTitulo("Datos del examen");

        campoTitulo = crearCampo(
                "Título",
                examen.getTitulo(),
                false
        );

        campoDescripcion = crearCampo(
                "Descripción",
                examen.getDescripcion(),
                true
        );

        agregarVista(campoTitulo);
        agregarVista(campoDescripcion);

        Button guardarExamen = botonPersonalizado(
                "Guardar examen",
                getColor(R.color.black),
                getColor(R.color.rosa)
        );

        guardarExamen.setOnClickListener(v -> {
            String titulo = campoTitulo.getText()
                    .toString().trim();

            String descripcion = campoDescripcion.getText()
                    .toString().trim();

            if (titulo.isEmpty()) {
                campoTitulo.setError("El titulo es obligatorio");
                return;
            }

            viewModel.guardarExamen(examenSlug, titulo, descripcion);
        });

        agregarVista(guardarExamen);

        agregarDivider();
        agregarTitulo("Preguntas y opciones");

        if (preguntas == null || preguntas.isEmpty()) {
            agregarTitulo("Este examen no tiene preguntas");
            return;
        }

        for (Pregunta pregunta : preguntas) {
            mostrarPregunta(pregunta);
        }
    }

    private void mostrarPregunta(Pregunta pregunta) {

        agregarDivider();

        agregarTitulo("Pregunta ");

        EditText enunciado = crearCampo(
                "Enunciado",
                pregunta.getEnunciado(),
                true
        );

        EditText puntos = crearCampo(
                "Puntuacion",
                String.valueOf(pregunta.getPuntos()),
                false
        );

        puntos.setInputType(
                android.text.InputType.TYPE_CLASS_NUMBER
                | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED
        );

        agregarVista(enunciado);
        agregarVista(puntos);

        Button guardarPregunta = botonPersonalizado(
                "Guardar pregunta",
                getColor(R.color.black),
                getColor(R.color.verde_claro)
        );

        guardarPregunta.setOnClickListener(v -> {

            String texto = enunciado.getText()
                    .toString().trim();

            String textoPuntos = puntos.getText()
                    .toString().trim()
                    .replace(',', '.');

            if (texto.isEmpty()) {
                enunciado.setError(
                        "El enunciado es obligatorio"
                );
                return;
            }

            double valorPuntos;

            try {
                valorPuntos = Double.parseDouble(textoPuntos);

                if (!Double.isFinite(valorPuntos) || valorPuntos < 0 || valorPuntos > 999.99) {
                    puntos.setError(
                            "Ingresa un valor entre 0 y 999"
                    );
                    return;
                }

            } catch (NumberFormatException e) {
                puntos.setError(
                        "Ingresa una puntuación válida"
                );
                return;
            }

            viewModel.guardarPregunta(pregunta, texto, valorPuntos);
        });

        agregarVista(guardarPregunta);

        TextView tituloOpciones = new TextView(this);
        tituloOpciones.setText("Opciones");
        tituloOpciones.setTextSize(18);
        tituloOpciones.setPadding(0, 16, 0, 8);
        tituloOpciones.setGravity(Gravity.CENTER);
        agregarVista(tituloOpciones);

        List<Opcion> opciones = pregunta.getOpciones();

        if (opciones == null || opciones.isEmpty()) {
            agregarTitulo("Esta pregunta no tiene opciones");
            return;
        }

        for (Opcion opcion : opciones) {
            mostrarOpcion(opcion);
        }
    }

    private void mostrarOpcion(Opcion opcion) {

        EditText textoOpcion = crearCampo("Texto de la opción ", opcion.getTextoOpcion(), true);

        CheckBox correcta =new CheckBox(this);
        correcta.setText("Es la respuesta correcta");
        correcta.setChecked(opcion.isEsCorrecta());

        Button guardarOpcion = botonPersonalizado(
                "Guardar opción",
                getColor(R.color.black),
                getColor(R.color.verde_claro)
        );

        guardarOpcion.setOnClickListener(v -> {

            String texto = textoOpcion.getText()
                    .toString().trim();

            if (texto.isEmpty()) {
                textoOpcion.setError(
                        "El texto no puede estar vacío"
                );
                return;
            }

            viewModel.guardarOpcion(opcion, texto, correcta.isChecked());
        });

        agregarVista(textoOpcion);
        agregarVista(correcta);
        agregarVista(guardarOpcion);
    }

    private EditText crearCampo(String pista, String valor, boolean multilinea) {

        EditText campo = new EditText(this);

        campo.setHint(pista);
        campo.setText(valor == null ? "" : valor);
        campo.setSingleLine(!multilinea);


        if (multilinea) {
            campo.setMinLines(2);
            campo.setGravity(android.view.Gravity.TOP);
        }

        return campo;
    }

    private void agregarTitulo(String texto) {
        TextView titulo= new TextView(this);
        titulo.setText(texto);
        titulo.setTextSize(18);
        titulo.setTextColor(getColor(R.color.rosa));
        titulo.setGravity(Gravity.CENTER);

        titulo.setPadding(0, 18, 0, 10);

        agregarVista(titulo);
    }

    private void agregarVista(View vista) {
        contenedorFormulario.addView(vista);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
        vista.getLayoutParams();

        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;

        params.bottomMargin = 8;
        params.leftMargin = 2;
        params.rightMargin =2;
        vista.setLayoutParams(params);
    }

    private void agregarDivider() {
        View separador = new View(this);
        separador.setBackgroundColor(getColor(R.color.beige_oscuro));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);

        params.topMargin = 13;
        params.bottomMargin = 13;

        contenedorFormulario.addView(separador, params);
    }


    private Button botonPersonalizado(String texto, int colorFondo, int colorTexto) {

        Button boton = new Button(this);

        boton.setText(texto);
        boton.setTextColor(colorTexto);
        boton.setTextSize(15);
        boton.setTypeface(null, Typeface.BOLD);
        boton.setAllCaps(false);
        boton.setGravity(Gravity.CENTER);

        // Fondo personalizado
        GradientDrawable fondo = new GradientDrawable();
        fondo.setColor(colorFondo);
        fondo.setCornerRadius(12);
        fondo.setStroke(
                dp(1),
                oscurecerColor(colorFondo)
        );

        boton.setBackground(fondo);

        // inner padding
        boton.setPadding(
                dp(16),
                dp(10),
                dp(16),
                dp(10)
        );

        // Elimina la elevación predeterminada
        boton.setElevation(0);


        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                dp(6),
                0,
                dp(10)
        );

        boton.setLayoutParams(params);

        return boton;
    }

    private int dp(float valor) {
        return (int) (
                valor * getResources()
                        .getDisplayMetrics().density + 0.5f
        );
    }

    private int oscurecerColor(int color) {
        return Color.rgb(
                (int) (Color.red(color) * 0.85f),
                (int) (Color.green(color) * 0.85f),
                (int) (Color.blue(color) * 0.85f)
        );
    }


}