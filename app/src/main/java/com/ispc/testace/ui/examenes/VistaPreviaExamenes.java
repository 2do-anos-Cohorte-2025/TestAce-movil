package com.ispc.testace.ui.examenes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ispc.testace.R;
import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.ui.examenes.intento.IntentoActivity;
import com.ispc.testace.ui.examenes.modificar.ModificarExamenActivity;

import android.view.View;
import android.widget.Toast;

public class VistaPreviaExamenes extends AppCompatActivity {

    private TextView txtTitulo;
    private TextView txtDescripcion;
    private String examenId;
    private Integer duracion_examen;

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
            duracion_examen = intentRecibido.getIntExtra("EXAMEN_TIEMPO",30);

            if (nombreExamen != null) {
                txtTitulo.setText(nombreExamen);
            }
            if (descExamen != null) {
                txtDescripcion.setText(descExamen);
            }
        }

        btnComenzar.setOnClickListener(v -> {

            Intent intent = new Intent(VistaPreviaExamenes.this, IntentoActivity.class);

            intent.putExtra("examen_id", Integer.parseInt(examenId));
            Integer usuarioId = TokenManager.getUsuarioId();
            intent.putExtra("duracion_examen",duracion_examen); // Tiempo limite del examen actual


            if (usuarioId != null && TokenManager.estaLogueado()) {
                intent.putExtra("usuario_id", usuarioId);
            }
            startActivity(intent);
        });
        btnFlechitaVolver.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}