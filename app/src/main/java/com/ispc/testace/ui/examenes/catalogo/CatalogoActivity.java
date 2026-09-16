package com.ispc.testace.ui.examenes.catalogo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;


import com.ispc.testace.R;
import com.google.android.material.chip.Chip;
import android.widget.PopupMenu;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
public class CatalogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Chip chipCategoria = findViewById(R.id.chipCategoria);
        chipCategoria.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CatalogoActivity.this, v);
            String[] categorias = getResources().getStringArray(R.array.categorias_array);
            for (String categoria : categorias) {
                popup.getMenu().add(categoria);
            }
            popup.setOnMenuItemClickListener(item -> {
                chipCategoria.setText(item.getTitle());
                return true;
            });
            popup.show();
        });

        Chip chipNivel = findViewById(R.id.chipNivel);
        chipNivel.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CatalogoActivity.this, v);
            String[] nivel = getResources().getStringArray(R.array.niveles_array);
            for (String Nivel : nivel) {
                popup.getMenu().add(Nivel);
            }
            popup.setOnMenuItemClickListener(item -> {
                chipNivel.setText(item.getTitle());
                return true;
            });
            popup.show();
        });

        Chip chipCreador = findViewById(R.id.chipCreador);
        chipCreador.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CatalogoActivity.this, v);
            String[] creador = getResources().getStringArray(R.array.creador_array);
            for (String Creador : creador) {
                popup.getMenu().add(Creador);
            }
            popup.setOnMenuItemClickListener(item -> {
                chipCreador.setText(item.getTitle());
                return true;
            });
            popup.show();
        });

        Button btnVerMas = findViewById(R.id.btnVerMas);
        btnVerMas.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, com.ispc.testace.ui.examenes.VistaPreviaExamenes.class);

            intent.putExtra("EXAMEN_ID", "1");
            intent.putExtra("EXAMEN_NOMBRE", "Exámen evaluativo");
            intent.putExtra("EXAMEN_DESCRIPCION", "Este examen consta de 10 preguntas de opción múltiple. Tienes un límite de tiempo de 30 minutos. Asegúrate de revisar tus respuestas antes de enviar.");

            startActivity(intent);
        });



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}