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
            // HOLA
            //Cristian, despues agrega el examen id por aca y mandalo en el intent.putExtra porfa :)
            // Despues borrar
            Intent intent = new Intent(VistaPreviaExamenes.this, IntentoActivity.class);
            intent.putExtra("examen_id", 1); // ID del examen seleccionado
            Integer usuarioId = TokenManager.getUsuarioId(); //ID del usuario || usuario null si es un invitado

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