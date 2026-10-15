package com.ispc.testace.ui.home.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ispc.testace.R;
import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.data.model.Usuario;
import com.ispc.testace.databinding.FragmentHomeBinding;
import com.ispc.testace.ui.examenes.catalogo.CatalogoActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Saludo personalizado: usa el nombre guardado al iniciar sesión
        Usuario usuario = TokenManager.getUsuario();
        if (TokenManager.estaLogueado() && usuario != null
                && usuario.getNombre() != null && !usuario.getNombre().trim().isEmpty()) {
            binding.tvSaludo.setText(
                    getString(R.string.home_saludo_usuario, usuario.getNombre().trim()));
        } else {
            binding.tvSaludo.setText(R.string.home_saludo_invitado);
        }

        // Botón: abre el catálogo de exámenes
        binding.btnExplorar.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), CatalogoActivity.class)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}