package com.ispc.testace.ui.contacto;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ispc.testace.R;

public class ContactoActivity extends AppCompatActivity {

    private static final String EMAIL_DESTINO = "testace.404@gmail.com";

    private EditText edtNombre, edtEmail, edtAsunto, edtMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edtNombre = findViewById(R.id.edtNombreContacto);
        edtEmail = findViewById(R.id.edtEmailContacto);
        edtAsunto = findViewById(R.id.edtAsuntoContacto);
        edtMensaje = findViewById(R.id.edtMensajeContacto);
        Button btnEnviar = findViewById(R.id.btnEnviarContacto);

        btnEnviar.setOnClickListener(v -> validarYConfirmar());
    }

    private void validarYConfirmar() {
        String nombre = edtNombre.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String asunto = edtAsunto.getText().toString().trim();
        String mensaje = edtMensaje.getText().toString().trim();

        edtNombre.setError(null);
        edtEmail.setError(null);
        edtAsunto.setError(null);
        edtMensaje.setError(null);

        boolean ok = true;
        if (nombre.isEmpty()) { edtNombre.setError("Ingresá tu nombre"); ok = false; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { edtEmail.setError("Email inválido"); ok = false; }
        if (asunto.isEmpty()) { edtAsunto.setError("Ingresá un asunto"); ok = false; }
        if (mensaje.isEmpty()) { edtMensaje.setError("Escribí tu mensaje"); ok = false; }
        if (!ok) return;

        new AlertDialog.Builder(this)
                .setTitle("Enviar mensaje")
                .setMessage("Serás redireccionado a tu app de correo para enviar el mensaje. ¿Querés continuar?")
                .setPositiveButton("Aceptar", (dialog, which) ->
                        abrirAppDeCorreo(nombre, email, asunto, mensaje))
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void abrirAppDeCorreo(String nombre, String email, String asunto, String mensaje) {
        String cuerpo = mensaje + "\n\n--\n" + nombre + " <" + email + ">";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));   // solo apps de mail
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_DESTINO});
        intent.putExtra(Intent.EXTRA_SUBJECT, "[TestAce] " + asunto);
        intent.putExtra(Intent.EXTRA_TEXT, cuerpo);

        try {
            startActivity(Intent.createChooser(intent, "Enviar con..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,
                    "No hay ninguna app de correo instalada",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}