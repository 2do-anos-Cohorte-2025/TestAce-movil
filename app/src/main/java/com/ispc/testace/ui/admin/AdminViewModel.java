package com.ispc.testace.ui.admin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ispc.testace.data.model.ExamenCatalogo;
import com.ispc.testace.data.model.Usuario;
import com.ispc.testace.data.repository.ExamenRepository;
import com.ispc.testace.data.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewModel extends ViewModel {

    public static final int SECCION_USUARIOS = 0;
    public static final int SECCION_EXAMENES = 1;

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final ExamenRepository examenRepository = new ExamenRepository();

    private final MutableLiveData<List<Usuario>> usuarios =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<ExamenCatalogo>> examenes =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> seccion = new MutableLiveData<>(SECCION_USUARIOS);
    private final MutableLiveData<Boolean> cargando = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> guardando = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> guardadoExitoso = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    private int peticionesPendientes = 0;

    public MutableLiveData<List<Usuario>> getUsuarios() {
        return usuarios;
    }

    public MutableLiveData<List<ExamenCatalogo>> getExamenes() {
        return examenes;
    }

    public MutableLiveData<Integer> getSeccion() {
        return seccion;
    }

    public MutableLiveData<Boolean> getCargando() {
        return cargando;
    }

    public MutableLiveData<Boolean> getGuardando() {
        return guardando;
    }

    public MutableLiveData<Boolean> getGuardadoExitoso() {
        return guardadoExitoso;
    }

    public MutableLiveData<String> getMensaje() {
        return mensaje;
    }

    public void setSeccion(int nueva) {
        seccion.setValue(nueva);
    }

    public void limpiarMensaje() {
        mensaje.setValue(null);
    }

    public void consumirGuardadoExitoso() {
        guardadoExitoso.setValue(false);
    }

    /* ---------------- Carga de listas ---------------- */

    public void cargarTodo() {
        cargarUsuarios();
        cargarExamenes();
    }

    private void iniciarCarga() {
        peticionesPendientes++;
        cargando.setValue(true);
    }

    private void terminarCarga() {
        peticionesPendientes--;
        if (peticionesPendientes <= 0) {
            peticionesPendientes = 0;
            cargando.setValue(false);
        }
    }

    public void cargarUsuarios() {
        iniciarCarga();
        usuarioRepository.getUsuarios(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                terminarCarga();
                if (response.isSuccessful() && response.body() != null) {
                    usuarios.setValue(response.body());
                } else {
                    mensaje.setValue(mensajeDeError(response));
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                terminarCarga();
                mensaje.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void cargarExamenes() {
        iniciarCarga();
        examenRepository.getExamenes(new Callback<List<ExamenCatalogo>>() {
            @Override
            public void onResponse(Call<List<ExamenCatalogo>> call, Response<List<ExamenCatalogo>> response) {
                terminarCarga();
                if (response.isSuccessful() && response.body() != null) {
                    examenes.setValue(response.body());
                } else {
                    mensaje.setValue(mensajeDeError(response));
                }
            }

            @Override
            public void onFailure(Call<List<ExamenCatalogo>> call, Throwable t) {
                terminarCarga();
                mensaje.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    /* ---------------- Eliminar ---------------- */

    public void eliminarUsuario(Usuario usuario) {
        usuarioRepository.deleteUsuario(usuario.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    List<Usuario> nueva = copiaUsuarios();
                    nueva.remove(usuario);
                    usuarios.setValue(nueva);
                    mensaje.setValue("Usuario eliminado.");
                    cargarExamenes();
                } else {
                    mensaje.setValue(mensajeDeError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mensaje.setValue("Error de conexión al eliminar el usuario.");
            }
        });
    }

    public void eliminarExamen(ExamenCatalogo examen) {
        examenRepository.deleteExamen(examen.getSlug(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    List<ExamenCatalogo> actual = examenes.getValue();
                    List<ExamenCatalogo> nueva = actual != null ? new ArrayList<>(actual) : new ArrayList<>();
                    nueva.remove(examen);
                    examenes.setValue(nueva);
                    mensaje.setValue("Examen eliminado.");
                } else {
                    mensaje.setValue(mensajeDeError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mensaje.setValue("Error de conexión al eliminar el examen.");
            }
        });
    }

    /* ---------------- Crear usuario ---------------- */

    public void crearUsuario(Map<String, Object> datos) {
        guardando.setValue(true);
        usuarioRepository.createUsuario(datos, new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                guardando.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Usuario> nueva = copiaUsuarios();
                    nueva.add(response.body());
                    usuarios.setValue(nueva);
                    mensaje.setValue("Usuario creado.");
                    guardadoExitoso.setValue(true);
                } else {
                    mensaje.setValue(mensajeDeError(response));
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                guardando.setValue(false);
                mensaje.setValue("Error de conexión al crear el usuario.");
            }
        });
    }

    /* ---------------- Editar usuario ---------------- */

    public void editarUsuario(Usuario usuario, Map<String, Object> datos) {
        guardando.setValue(true);
        usuarioRepository.updateUsuario(usuario.getId(), datos, new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                guardando.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    Usuario actualizado = response.body();
                    List<Usuario> nueva = copiaUsuarios();
                    for (int i = 0; i < nueva.size(); i++) {
                        if (nueva.get(i).getId() == actualizado.getId()) {
                            nueva.set(i, actualizado);
                            break;
                        }
                    }
                    usuarios.setValue(nueva);
                    mensaje.setValue("Usuario actualizado.");
                    guardadoExitoso.setValue(true);
                } else {
                    mensaje.setValue(mensajeDeError(response));
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                guardando.setValue(false);
                mensaje.setValue("Error de conexión al guardar los cambios.");
            }
        });
    }

    /* ---------------- Auxiliares ---------------- */

    private List<Usuario> copiaUsuarios() {
        List<Usuario> actual = usuarios.getValue();
        return actual != null ? new ArrayList<>(actual) : new ArrayList<>();
    }

    private String mensajeDeError(Response<?> response) {
        int codigo = response.code();
        if (codigo == 401) return "Tu sesión expiró. Volvé a iniciar sesión.";
        if (codigo == 403) return "No tenés permisos de administrador para hacer esto.";
        if (codigo == 404) return "El elemento ya no existe.";
        if (codigo == 400) {
            String detalle = detalleDeValidacion(response);
            if (detalle != null) return detalle;
        }
        return "Ocurrió un error. Código: " + codigo;
    }

    private String detalleDeValidacion(Response<?> response) {
        try {
            if (response.errorBody() == null) return null;
            JsonObject json = new Gson().fromJson(response.errorBody().string(), JsonObject.class);
            if (json == null) return null;

            if (json.has("username")) return "Ya existe un usuario con ese email.";
            if (json.has("password")) return "Contraseña no válida: " + unir(json.get("password"));
            if (json.has("email")) return "Email no válido: " + unir(json.get("email"));
            if (json.has("rol")) return "Rol no válido.";
        } catch (Exception e) {
        }
        return null;
    }

    private String unir(JsonElement elemento) {
        if (elemento.isJsonArray()) {
            JsonArray arreglo = elemento.getAsJsonArray();
            StringBuilder sb = new StringBuilder();
            for (JsonElement e : arreglo) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(e.getAsString());
            }
            return sb.toString();
        }
        return elemento.getAsString();
    }
}