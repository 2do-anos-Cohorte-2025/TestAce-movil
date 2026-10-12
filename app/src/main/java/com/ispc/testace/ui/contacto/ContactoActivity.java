package com.ispc.testace.ui.contacto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ispc.testace.R;
import android.widget.Toast;
public class ContactoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        findViewById(R.id.btnEnviarContacto).setOnClickListener(v -> {
            Toast.makeText(ContactoActivity.this,
                    "Mensaje enviado (pendiente)",
                    Toast.LENGTH_SHORT).show();
        });
    }
}