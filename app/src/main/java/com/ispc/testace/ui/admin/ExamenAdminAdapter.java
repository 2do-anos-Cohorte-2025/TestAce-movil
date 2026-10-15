package com.ispc.testace.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.testace.R;
import com.ispc.testace.data.model.ExamenCatalogo;

import java.util.ArrayList;
import java.util.List;

public class ExamenAdminAdapter extends RecyclerView.Adapter<ExamenAdminAdapter.ViewHolder> {

    public interface Listener {
        void onEliminar(ExamenCatalogo examen);
    }

    private final List<ExamenCatalogo> items = new ArrayList<>();
    private final Listener listener;

    public ExamenAdminAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<ExamenCatalogo> nuevos) {
        items.clear();
        if (nuevos != null) items.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_examen_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        ExamenCatalogo e = items.get(position);

        h.tvTitulo.setText(e.getTitulo());
        h.tvCreador.setText("Creado por: "
                + (e.getUsuarioNombre() != null ? e.getUsuarioNombre() : "—")
                + (e.isEsProfesor() ? " (profesor)" : ""));
        h.tvDetalle.setText((e.getCategoriaNombre() != null ? e.getCategoriaNombre() : "Sin categoría")
                + " · "
                + (e.getNivelNombre() != null ? e.getNivelNombre() : "Sin nivel"));

        h.btnEliminar.setOnClickListener(v -> listener.onEliminar(e));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvCreador, tvDetalle;
        Button btnEliminar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloExamenAdmin);
            tvCreador = itemView.findViewById(R.id.tvCreadorExamenAdmin);
            tvDetalle = itemView.findViewById(R.id.tvDetalleExamenAdmin);
            btnEliminar = itemView.findViewById(R.id.btnEliminarExamenAdmin);
        }
    }
}
