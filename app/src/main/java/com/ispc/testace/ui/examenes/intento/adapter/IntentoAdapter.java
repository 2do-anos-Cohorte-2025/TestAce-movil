package com.ispc.testace.ui.examenes.intento.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ispc.testace.R;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.ui.examenes.intento.IntentoViewModel;

import java.util.ArrayList;
import java.util.List;

public class IntentoAdapter extends RecyclerView.Adapter<IntentoAdapter.IntentoViewHolder> {
    private List<Pregunta> preguntas;
    private int currentPosition = 0;
    private IntentoViewModel viewModel;
    private List<Opcion> opcionesActuales = new ArrayList<>();


    public IntentoAdapter(IntentoViewModel viewModel) {
        this.viewModel = viewModel;
        this.preguntas = new ArrayList<>();

    }

    @NonNull
    @Override
    public IntentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intento, parent, false);
        return new IntentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntentoViewHolder holder, int position) {
        if (preguntas != null && !preguntas.isEmpty()) {
            Pregunta pregunta = preguntas.get(currentPosition);
            holder.bind(pregunta);
        } else {
            holder.mostrarVistaVacia();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas != null ? preguntas : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
        notifyDataSetChanged();
    }

    public void setOpciones(List<Opcion> opciones) {
        opcionesActuales = opciones != null
                ? opciones
                : new ArrayList<>();

        notifyDataSetChanged();
    }


    public class IntentoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewEnunciado;
        private ImageView imageViewPregunta;
        private RecyclerView recyclerViewOpciones;
        private EditText editTextRespuestaTexto;
        private OpcionAdapter opcionAdapter;
        private TextWatcher respuestaWatcher;

        public void mostrarVistaVacia() {
            textViewEnunciado.setText("No hay preguntas disponibles");
            imageViewPregunta.setVisibility(View.GONE);
            recyclerViewOpciones.setVisibility(View.GONE);
            editTextRespuestaTexto.setVisibility(View.GONE);
        }

        public IntentoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEnunciado = itemView.findViewById(R.id.textViewEnunciado);
            imageViewPregunta = itemView.findViewById(R.id.imageViewPregunta);
            recyclerViewOpciones = itemView.findViewById(R.id.recyclerViewOpciones);
            editTextRespuestaTexto = itemView.findViewById(R.id.editTextRespuestaTexto);


            opcionAdapter = new OpcionAdapter(new ArrayList<>(),opcionId -> {
                Pregunta pregunta = preguntas.get(currentPosition);


                viewModel.guardarSeleccionOpcion(
                        pregunta.getId(),
                        opcionId
                );
                viewModel.guardarRespuesta(
                        pregunta.getId(),
                        opcionId,
                        null
                        );
                    }
            );
            recyclerViewOpciones.setAdapter(opcionAdapter);
            recyclerViewOpciones.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        public void bind(Pregunta pregunta) {
            textViewEnunciado.setText(pregunta.getEnunciado());

            if (pregunta.getImagen_pregunta() != null) {
                Glide.with(itemView.getContext()).load(pregunta.getImagen_pregunta()).into(imageViewPregunta);
                imageViewPregunta.setVisibility(View.VISIBLE);
            } else {
                imageViewPregunta.setVisibility(View.GONE);
            }

            if (pregunta.getTipo().equals("opcion_multiple") || pregunta.getTipo().equals("verdadero_falso")) {
                recyclerViewOpciones.setVisibility(View.VISIBLE);
                editTextRespuestaTexto.setVisibility(View.GONE);

                opcionAdapter.updateOpciones(opcionesActuales);
            } else if (pregunta.getTipo().equals("numerico") || pregunta.getTipo().equals("texto")) {

                recyclerViewOpciones.setVisibility(View.GONE);
                editTextRespuestaTexto.setVisibility(View.VISIBLE);
                String respuestaGuardada = viewModel.getRespuestaTexto(viewModel.getPreguntaActualIndex().getValue());
                editTextRespuestaTexto.setText(
                        respuestaGuardada != null ? respuestaGuardada : ""
                );
                editTextRespuestaTexto.setHint(
                        pregunta.getTipo().equals("numerico")
                                ? "Escribe tu respuesta numérica"
                                : "Escribe tu respuesta"
                );

                if (respuestaWatcher != null) {
                    editTextRespuestaTexto.removeTextChangedListener(
                            respuestaWatcher
                    );
                }

                respuestaWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String respuesta = s.toString().trim();
                        if(!respuesta.isEmpty()) {
                            viewModel.guardarRespuestaTexto(
                                    pregunta.getId(),
                                    s.toString()
                            );
                        }
                    }
                };

                editTextRespuestaTexto.addTextChangedListener(respuestaWatcher);
            }
        }
    }
}
