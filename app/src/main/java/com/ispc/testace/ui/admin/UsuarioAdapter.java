package com.ispc.testace.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ispc.testace.R;
import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.data.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    public interface Listener {
        void onEditar(Usuario usuario);

        void onEliminar(Usuario usuario);
    }

    private final List<Usuario> items = new ArrayList<>();
    private final Listener listener;

    public UsuarioAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<Usuario> nuevos) {
        items.clear();
        if (nuevos != null) items.addAll(nuevos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Usuario u = items.get(position);

        Integer miId = TokenManager.getUsuarioId();
        boolean esYo = miId != null && miId.intValue() == u.getId();

        h.tvNombre.setText(u.getNombreCompleto() + (esYo ? " (vos)" : ""));
        h.tvEmail.setText(u.getEmail());
        h.tvRol.setText("Rol: " + u.getRol());

        // El admin no puede borrarse a sí mismo desde el panel
        h.btnEliminar.setEnabled(!esYo);

        h.btnEditar.setOnClickListener(v -> listener.onEditar(u));
        h.btnEliminar.setOnClickListener(v -> listener.onEliminar(u));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEmail, tvRol;
        Button btnEditar, btnEliminar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreUsuario);
            tvEmail = itemView.findViewById(R.id.tvEmailUsuario);
            tvRol = itemView.findViewById(R.id.tvRolUsuario);
            btnEditar = itemView.findViewById(R.id.btnEditarUsuario);
            btnEliminar = itemView.findViewById(R.id.btnEliminarUsuario);
        }
    }
}
