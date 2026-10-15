package com.ispc.testace.ui.examenes.modificar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.repository.ExamenRepository;
import com.ispc.testace.data.repository.OpcionRepository;
import com.ispc.testace.data.repository.PreguntaRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ModificarExamenViewModel extends ViewModel {
    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;
    private OpcionRepository opcionRepository;



    public ModificarExamenViewModel() {
        this.examenRepository = new ExamenRepository();
        this.preguntaRepository = new PreguntaRepository();
        this.opcionRepository = new OpcionRepository();
    }
    private final MutableLiveData<Examen> examen =
            new MutableLiveData<>();

    private final MutableLiveData<List<Pregunta>> preguntas =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Boolean> cargando =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> mensaje =
            new MutableLiveData<>();

    private int peticionesPendientes = 0;

    /* Métodos para conseguir las variables */

    public MutableLiveData<Examen> getExamen() {
        return examen;
    }

    public MutableLiveData<List<Pregunta>> getPreguntas() {
        return preguntas;
    }

    public MutableLiveData<Boolean> getCargando() {
        return cargando;
    }

    public MutableLiveData<String> getMensaje() {
        return mensaje;
    }



    // Funciones

    public void cargarExamen(String slug) {
        cargando.setValue(true);

        examenRepository.getExamenBySlug(slug, new Callback<Examen>() {
                    @Override
                    public void onResponse(
                            Call<Examen> call,
                            Response<Examen> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            cargando.setValue(false);
                            mensaje.setValue(
                                    "No se pudo cargar el examen. Código: " + response.code()
                            );
                            return;
                        }

                        Examen resultado = response.body();
                        examen.setValue(resultado);

                        cargarPreguntas(resultado.getId());
                    }

                    @Override
                    public void onFailure(Call<Examen> call, Throwable t) {
                        cargando.setValue(false);
                        mensaje.setValue("Error de conexión: " + t.getMessage());
                    }
                }
        );
    }

    private void cargarPreguntas(int examenId) {
        preguntaRepository.getPreguntasByExamen(examenId, new Callback<List<Pregunta>>() {
                    @Override
                    public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            cargando.setValue(false);
                            mensaje.setValue("No se pudieron cargar las preguntas.");
                            return;
                        }

                        List<Pregunta> lista = response.body();
                        preguntas.setValue(lista);

                        if (lista.isEmpty()) {
                            cargando.setValue(false);
                            return;
                        }

                        peticionesPendientes = lista.size();

                        for (Pregunta pregunta : lista) {
                            cargarOpciones(pregunta);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Pregunta>> call, Throwable t) {

                        cargando.setValue(false);
                        mensaje.setValue("Error al cargar las preguntas: " + t.getMessage()
                        );
                    }
                }
        );
    }

    private void cargarOpciones(Pregunta pregunta) {
        opcionRepository.getOpcionesByPregunta(pregunta.getId(), new Callback<List<Opcion>>() {
                    @Override
                    public void onResponse(Call<List<Opcion>> call, Response<List<Opcion>> response) {

                        if (!response.isSuccessful() || response.body() == null) {
                            mensaje.setValue("No se pudieron cargar las opciones " + "de la pregunta "
                                    + pregunta.getId());
                        } else {
                            pregunta.setOpciones(response.body());
                            preguntas.setValue(
                                    new ArrayList<>(preguntas.getValue())
                            );
                        }

                        terminarCargaOpciones();
                    }

                    @Override
                    public void onFailure(Call<List<Opcion>> call, Throwable t) {
                        mensaje.setValue("Error al cargar opciones: " + t.getMessage());
                        terminarCargaOpciones();
                    }
                }
        );
    }



    private void terminarCargaOpciones() {
        peticionesPendientes--;

        if (peticionesPendientes <= 0) {
            cargando.setValue(false);
            preguntas.setValue(
                    new ArrayList<>(preguntas.getValue())
            );
        }
    }

    public void guardarExamen(
            String slug,
            String titulo,
            String descripcion) {

        Map<String, Object> datos = new HashMap<>();
        datos.put("titulo", titulo);
        datos.put("descripcion", descripcion);

        examenRepository.updateExamen(slug, datos, new Callback<Examen>() {
                    @Override
                    public void onResponse(Call<Examen> call, Response<Examen> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                examen.setValue(response.body());
                            }
                            mensaje.setValue(
                                    "Datos del examen guardados."
                            );
                        } else {
                            mensaje.setValue("Error al guardar el examen. Código: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Examen> call, Throwable t) {

                        mensaje.setValue("Error de conexión al guardar el examen.");
                    }
                }
        );
    }

    public void guardarPregunta(Pregunta pregunta, String enunciado, double puntos) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("enunciado", enunciado);
        datos.put("puntos", puntos);

        preguntaRepository.updatePregunta(pregunta.getId(), datos, new Callback<Pregunta>() {
            @Override
            public void onResponse(Call<Pregunta> call, Response<Pregunta> response) {

                if (response.isSuccessful()) {
                    pregunta.setEnunciado(enunciado);
                    pregunta.setPuntos(puntos);

                    mensaje.setValue("Pregunta guardada.");
                } else {
                    mensaje.setValue("Error al guardar la pregunta.");
                }
            }

            @Override
            public void onFailure(Call<Pregunta> call, Throwable t) {
                mensaje.setValue("Error de conexión al guardar la pregunta.");
            }
        });
    }

    public void guardarOpcion(Opcion opcion, String texto, boolean esCorrecta) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("texto_opcion", texto);
        datos.put("es_correcta", esCorrecta);

        opcionRepository.updateOpcion(opcion.getId(), datos, new Callback<Opcion>() {
            @Override
            public void onResponse(Call<Opcion> call, Response<Opcion> response) {
                if (response.isSuccessful()) {
                    opcion.setTextoOpcion(texto);
                    opcion.setEsCorrecta(esCorrecta);

                    mensaje.setValue("Opcion guardada.");
                } else {
                    mensaje.setValue("Error al guardar la opcion." );
                }
            }

            @Override
            public void onFailure(Call<Opcion> call, Throwable t) {
                mensaje.setValue("Error de conexión al guardar la opcion.");
            }
        });
    }


}


