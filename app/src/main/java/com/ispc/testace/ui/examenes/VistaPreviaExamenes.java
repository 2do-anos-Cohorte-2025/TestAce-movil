package com.ispc.testace.ui.examenes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ispc.testace.R;
import com.ispc.testace.ui.examenes.intento.IntentoActivity;
import android.view.View;

public class VistaPreviaExamenes extends AppCompatActivity {

    private TextView txtTitulo;
    private TextView txtDescripcion;
    private String examenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_previa_examenes);


        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        Button btnComenzar = findViewById(R.id.btnComenzar);

        View btnFlechitaVolver = findViewById(R.id.btnFlechitaVolver);

        Intent intentRecibido = getIntent();
        if (intentRecibido != null) {
            examenId = intentRecibido.getStringExtra("EXAMEN_ID");
            String nombreExamen = intentRecibido.getStringExtra("EXAMEN_NOMBRE");
            String descExamen = intentRecibido.getStringExtra("EXAMEN_DESCRIPCION");


            if (nombreExamen != null) {
                txtTitulo.setText(nombreExamen);
            }
            if (descExamen != null) {
                txtDescripcion.setText(descExamen);
            }
        }

        btnComenzar.setOnClickListener(v -> {
            Intent intent = new Intent(VistaPreviaExamenes.this, IntentoActivity.class);

            intent.putExtra("EXAMEN_ID", examenId);
            startActivity(intent);
            finish();
        });
        btnFlechitaVolver.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}