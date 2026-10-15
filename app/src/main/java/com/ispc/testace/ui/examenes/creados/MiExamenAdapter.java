package com.ispc.testace.ui.examenes.creados;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ispc.testace.R;
import com.ispc.testace.data.model.ExamenCatalogo;

import java.util.ArrayList;
import java.util.List;

public class MiExamenAdapter extends RecyclerView.Adapter<MiExamenAdapter.ViewHolder> {

    public interface Listener {
        void onVerMas(ExamenCatalogo examen);
        void onEditar(ExamenCatalogo examen);
        void onEliminar(ExamenCatalogo examen);
    }

    private final List<ExamenCatalogo> items = new ArrayList<>();
    private final Listener listener;

    public MiExamenAdapter(Listener listener) {
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
                .inflate(R.layout.item_mi_examen, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        ExamenCatalogo e = items.get(position);

        h.tvNombre.setText(e.getTitulo());
        h.imgProfesor.setVisibility(e.isEsProfesor() ? View.VISIBLE : View.GONE);
        h.tvCategoria.setText("Categoría: " +
                (e.getCategoriaNombre() != null ? e.getCategoriaNombre() : "Sin categoría"));
        h.tvNivel.setText("Nivel: " +
                (e.getNivelNombre() != null ? e.getNivelNombre() : "Sin nivel"));
        h.tvTiempo.setText(e.getTiempoLimite() != null
                ? "Tiempo límite: " + e.getTiempoLimite() + " min"
                : "Sin tiempo límite");

        if (e.getImagenExamen() != null && !e.getImagenExamen().isEmpty()) {
            Glide.with(h.itemView.getContext())
                    .load(e.getImagenExamen())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(h.img);
        } else {
            h.img.setImageResource(R.drawable.ic_launcher_background);
        }

        h.btnVerMas.setOnClickListener(v -> listener.onVerMas(e));

        h.btnOpciones.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.getMenu().add(0, 1, 0, "Editar");
            popup.getMenu().add(0, 2, 1, "Eliminar");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 1) {
                    listener.onEditar(e);
                } else {
                    listener.onEliminar(e);
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, imgProfesor;
        ImageButton btnOpciones;
        TextView tvNombre, tvCategoria, tvNivel, tvTiempo;
        Button btnVerMas;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgExamen);
            imgProfesor = itemView.findViewById(R.id.imgProfesor);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
            tvNombre = itemView.findViewById(R.id.tvNombreExamen);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvNivel = itemView.findViewById(R.id.tvNivel);
            tvTiempo = itemView.findViewById(R.id.tvTiempoLimite);
            btnVerMas = itemView.findViewById(R.id.btnVerMas);
        }
    }
}