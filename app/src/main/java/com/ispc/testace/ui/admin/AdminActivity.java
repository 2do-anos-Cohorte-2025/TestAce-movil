package com.ispc.testace.ui.admin;

import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ispc.testace.R;
import com.ispc.testace.data.local.TokenManager;
import com.ispc.testace.data.model.ExamenCatalogo;
import com.ispc.testace.data.model.Usuario;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private static final String[] ROLES = {"estudiante", "profesor", "administrador"};

    private AdminViewModel viewModel;
    private UsuarioAdapter usuarioAdapter;
    private ExamenAdminAdapter examenAdapter;
    private RecyclerView rv;
    private ProgressBar progress;
    private TextView tvVacio;
    private Button btnNuevoUsuario;
    private AlertDialog dialogoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!esAdministrador()) {
            Toast.makeText(this, "Solo los administradores pueden entrar acá.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rv = findViewById(R.id.rvAdmin);
        progress = findViewById(R.id.progressAdmin);
        tvVacio = findViewById(R.id.tvVacioAdmin);
        btnNuevoUsuario = findViewById(R.id.btnNuevoUsuario);
        TabLayout tabs = findViewById(R.id.tabsAdmin);

        usuarioAdapter = new UsuarioAdapter(new UsuarioAdapter.Listener() {
            @Override
            public void onEditar(Usuario usuario) {
                mostrarDialogoUsuario(usuario);
            }

            @Override
            public void onEliminar(Usuario usuario) {
                confirmarEliminarUsuario(usuario);
            }
        });
        examenAdapter = new ExamenAdminAdapter(this::confirmarEliminarExamen);

        rv.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        viewModel.getUsuarios().observe(this, lista -> {
            usuarioAdapter.setItems(lista);
            actualizarVacio();
        });

        viewModel.getExamenes().observe(this, lista -> {
            examenAdapter.setItems(lista);
            actualizarVacio();
        });

        viewModel.getSeccion().observe(this, seccion -> {
            boolean usuarios = seccion == AdminViewModel.SECCION_USUARIOS;
            rv.setAdapter(usuarios ? usuarioAdapter : examenAdapter);
            // El botón "+" solo existe en la sección de usuarios
            btnNuevoUsuario.setVisibility(usuarios ? View.VISIBLE : View.GONE);
            actualizarVacio();
        });

        viewModel.getCargando().observe(this, cargando -> {
            progress.setVisibility(Boolean.TRUE.equals(cargando) ? View.VISIBLE : View.GONE);
            actualizarVacio();
        });

        viewModel.getGuardando().observe(this, guardando -> {
            if (dialogoActual != null) {
                Button guardar = dialogoActual.getButton(AlertDialog.BUTTON_POSITIVE);
                if (guardar != null) guardar.setEnabled(!Boolean.TRUE.equals(guardando));
            }
        });

        viewModel.getGuardadoExitoso().observe(this, ok -> {
            if (Boolean.TRUE.equals(ok)) {
                if (dialogoActual != null) dialogoActual.dismiss();
                dialogoActual = null;
                viewModel.consumirGuardadoExitoso();
            }
        });

        viewModel.getMensaje().observe(this, texto -> {
            if (texto != null) {
                Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
                viewModel.limpiarMensaje();
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.setSeccion(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        Integer seccionGuardada = viewModel.getSeccion().getValue();
        TabLayout.Tab tab = tabs.getTabAt(seccionGuardada == null ? 0 : seccionGuardada);
        if (tab != null) tab.select();

        btnNuevoUsuario.setOnClickListener(v -> mostrarDialogoUsuario(null));

        if (savedInstanceState == null) {
            viewModel.cargarTodo();
        }
    }

    private boolean esAdministrador() {
        Usuario u = TokenManager.getUsuario();
        return TokenManager.estaLogueado() && u != null && "administrador".equals(u.getRol());
    }

    private void actualizarVacio() {
        Integer seccion = viewModel.getSeccion().getValue();
        boolean usuarios = seccion == null || seccion == AdminViewModel.SECCION_USUARIOS;

        List<?> lista = usuarios
                ? viewModel.getUsuarios().getValue()
                : viewModel.getExamenes().getValue();

        boolean vacio = (lista == null || lista.isEmpty())
                && !Boolean.TRUE.equals(viewModel.getCargando().getValue());

        tvVacio.setText(usuarios ? "No hay usuarios para mostrar" : "No hay exámenes para mostrar");
        tvVacio.setVisibility(vacio ? View.VISIBLE : View.GONE);
    }

    /* ---------------- Confirmaciones de eliminación ---------------- */

    private void confirmarEliminarUsuario(Usuario usuario) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar usuario")
                .setMessage("Se eliminará a " + usuario.getNombreCompleto()
                        + " y también los exámenes que creó. Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> viewModel.eliminarUsuario(usuario))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminarExamen(ExamenCatalogo examen) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar examen")
                .setMessage("Se eliminará \"" + examen.getTitulo()
                        + "\" junto con sus preguntas e intentos. Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> viewModel.eliminarExamen(examen))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    /* ---------------- Formulario de usuario ---------------- */

    private String textoDe(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }

    // existente == null  -> crear un usuario nuevo
    // existente != null  -> editar ese usuario
    private void mostrarDialogoUsuario(Usuario existente) {
        final boolean esEdicion = existente != null;
        Integer miId = TokenManager.getUsuarioId();
        final boolean esYo = esEdicion && miId != null && miId.intValue() == existente.getId();

        View vista = getLayoutInflater().inflate(R.layout.dialog_usuario, null);
        TextInputEditText etNombre = vista.findViewById(R.id.etNombre);
        TextInputEditText etApellido = vista.findViewById(R.id.etApellido);
        TextInputEditText etEmail = vista.findViewById(R.id.etEmail);
        TextInputEditText etPassword = vista.findViewById(R.id.etPassword);
        TextInputEditText etConfirmar = vista.findViewById(R.id.etConfirmarPassword);
        TextInputLayout tilNombre = vista.findViewById(R.id.tilNombre);
        TextInputLayout tilApellido = vista.findViewById(R.id.tilApellido);
        TextInputLayout tilEmail = vista.findViewById(R.id.tilEmail);
        TextInputLayout tilPassword = vista.findViewById(R.id.tilPassword);
        TextInputLayout tilConfirmar = vista.findViewById(R.id.tilConfirmarPassword);
        Spinner spinnerRol = vista.findViewById(R.id.spinnerRol);

        spinnerRol.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ROLES));

        if (esEdicion) {
            etNombre.setText(existente.getNombre());
            etApellido.setText(existente.getApellido());
            etEmail.setText(existente.getEmail());
            tilPassword.setHint("Nueva contraseña (opcional)");
            int posicion = Arrays.asList(ROLES).indexOf(existente.getRol());
            spinnerRol.setSelection(Math.max(posicion, 0));

            if (esYo) {
                spinnerRol.setEnabled(false);
            }
        } else {
            spinnerRol.setSelection(0); // estudiante por defecto
        }

        AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle(esEdicion ? "Editar usuario" : "Nuevo usuario")
                .setView(vista)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        dialogo.setOnShowListener(d ->
                dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    if (textoDe(etNombre).isEmpty()) {
                        tilNombre.setError("Ingresá el nombre.");
                        return;
                    }
                    tilNombre.setError(null);

                    if (textoDe(etApellido).isEmpty()) {
                        tilApellido.setError("Ingresá el apellido.");
                        return;
                    }
                    tilApellido.setError(null);
                    String email = textoDe(etEmail).toLowerCase(Locale.ROOT);
                    String password = etPassword.getText() == null ? "" : etPassword.getText().toString();
                    String confirmar = etConfirmar.getText() == null ? "" : etConfirmar.getText().toString();

                    if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        tilEmail.setError("Ingresá un correo electrónico válido.");
                        return;
                    }
                    tilEmail.setError(null);

                    boolean hayPassword = !esEdicion || !password.isEmpty() || !confirmar.isEmpty();
                    if (hayPassword) {
                        if (password.length() < 8) {
                            tilPassword.setError("Debe tener al menos 8 caracteres.");
                            return;
                        }
                        tilPassword.setError(null);
                        if (!password.equals(confirmar)) {
                            tilConfirmar.setError("Las contraseñas no coinciden.");
                            return;
                        }
                        tilConfirmar.setError(null);
                    }

                    Map<String, Object> datos = new HashMap<>();
                    datos.put("username", email);
                    datos.put("email", email);
                    datos.put("first_name", textoDe(etNombre));
                    datos.put("last_name", textoDe(etApellido));
                    if (hayPassword) datos.put("password", password);
                    if (!esYo) {
                        datos.put("rol", ROLES[spinnerRol.getSelectedItemPosition()]);
                    }

                    if (esEdicion) {
                        viewModel.editarUsuario(existente, datos);
                    } else {
                        viewModel.crearUsuario(datos);
                    }
                }));

        dialogo.setOnDismissListener(d -> {
            if (dialogoActual == dialogo) dialogoActual = null;
        });

        dialogoActual = dialogo;
        dialogo.show();
    }

    @Override
    protected void onDestroy() {
        if (dialogoActual != null) dialogoActual.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
