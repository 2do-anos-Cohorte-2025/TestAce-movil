package com.ispc.testace.ui.examenes.intento.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ispc.testace.R;
import com.ispc.testace.data.model.Opcion;
import java.util.List;
public class OpcionAdapter extends RecyclerView.Adapter<OpcionAdapter.OpcionViewHolder>  {
    private List<Opcion> opciones;
    private OnOpcionSelectedListener listener;

    private int selectedPosition = -1;

    public interface OnOpcionSelectedListener {
        void onOpcionSelected(int opcionId);
    }

    public OpcionAdapter(List<Opcion> opciones, OnOpcionSelectedListener listener) {
        this.opciones = opciones;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OpcionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opcion, parent, false);
        return new OpcionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpcionViewHolder holder, int position) {
        if (position < 0 || position >= opciones.size()) {
            holder.textViewOpcion.setText("");
            holder.imageViewOpcion.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.GONE);
            holder.radioButtonOpcion.setChecked(false);
            return;
        }

        Opcion opcion = opciones.get(position);

        holder.textViewOpcion.setText(opcion.getTextoOpcion());
        if (opcion.getImagenOpcion() != null && !opcion.getImagenOpcion().isEmpty()) {
            holder.imageViewOpcion.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(opcion.getImagenOpcion())
                    .placeholder(R.drawable.placeholder_imagen)
                    .into(holder.imageViewOpcion);
        } else {
            holder.imageViewOpcion.setVisibility(View.GONE);
        }

        // Actualizar el RadioButton directamente
        holder.radioButtonOpcion.setChecked(opcion.isSelected());

        // Configurar el clic en el RadioButton y en el itemView
        holder.radioButtonOpcion.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOpcionSelected(opcion.getId());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOpcionSelected(opcion.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return opciones.size();
    }

    public void updateOpciones(List<Opcion> nuevasOpciones) {
        this.opciones = nuevasOpciones;
        notifyDataSetChanged();
    }

    static class OpcionViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewOpcion;
        TextView textViewOpcion;
        RadioButton radioButtonOpcion;
        public OpcionViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOpcion = itemView.findViewById(R.id.imageViewOpcion);
            textViewOpcion = itemView.findViewById(R.id.textViewOpcion);
            radioButtonOpcion = itemView.findViewById(R.id.radioButtonOpcion);
        }

    }

}

