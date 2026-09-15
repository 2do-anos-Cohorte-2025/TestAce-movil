package com.ispc.testace;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout layoutPerfil;
    private LinearLayout layoutExamenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        layoutPerfil = findViewById(R.id.layoutPerfil);
        layoutExamenes = findViewById(R.id.layoutExamenes);

        setupTabs();
        setupEditableFields();
        setupSaveAndDeleteButtons();
    }

    private void setupTabs() {
        TabLayout tabLayout = findViewById(R.id.tabLayoutProfile);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    layoutPerfil.setVisibility(LinearLayout.VISIBLE);
                    layoutExamenes.setVisibility(LinearLayout.GONE);
                } else {
                    layoutPerfil.setVisibility(LinearLayout.GONE);
                    layoutExamenes.setVisibility(LinearLayout.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No-op
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No-op
            }
        });
    }

    private void setupEditableFields() {
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etNombre = findViewById(R.id.etNombre);
        EditText etApellido = findViewById(R.id.etApellido);

        Button btnEditUsername = findViewById(R.id.btnEditUsername);
        Button btnEditNombre = findViewById(R.id.btnEditNombre);
        Button btnEditApellido = findViewById(R.id.btnEditApellido);

        btnEditUsername.setOnClickListener(v -> enableEditing(etUsername));
        btnEditNombre.setOnClickListener(v -> enableEditing(etNombre));
        btnEditApellido.setOnClickListener(v -> enableEditing(etApellido));
    }

    private void enableEditing(EditText editText) {
        editText.setEnabled(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
    }

    private void setupSaveAndDeleteButtons() {
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnEliminarCuenta = findViewById(R.id.btnEliminarCuenta);

        // TODO: todavía no hay backend/base de datos, por eso no hace nada.
        btnGuardar.setOnClickListener(v -> {
            // Sin funcionalidad por el momento.
        });

        // TODO: falta definir la lógica de borrado de cuenta.
        btnEliminarCuenta.setOnClickListener(v -> {
            // Sin funcionalidad por el momento.
        });
    }
}