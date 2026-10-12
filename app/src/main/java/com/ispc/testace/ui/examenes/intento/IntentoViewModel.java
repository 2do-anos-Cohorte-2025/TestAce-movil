package com.ispc.testace.ui.examenes.intento;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.RespuestaUsuario;
import com.ispc.testace.data.model.Usuario;
import com.ispc.testace.data.repository.IntentoExamenRepository;
import com.ispc.testace.data.repository.OpcionRepository;
import com.ispc.testace.data.repository.PreguntaRepository;
import com.ispc.testace.data.repository.RespuestaUsuarioRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class IntentoViewModel extends ViewModel{
    private final IntentoExamenRepository intentoExamenRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaUsuarioRepository respuestaUsuarioRepository;
    private final OpcionRepository opcionRepository;

    protected long tiempoRestanteEnMilis;

    //Datos observables
    private final MutableLiveData<Pregunta> preguntaActualLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Opcion>> opcionesLiveData = new MutableLiveData<>();
    private final MutableLiveData<IntentoExamen> intentoExamenLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final MutableLiveData<Map<Integer, Integer>> respuestasSeleccionadas = new MutableLiveData<>(new HashMap<>());

    private final MutableLiveData<Integer> preguntaIndex = new MutableLiveData<>(0); // Valor inicial 0
    private final MutableLiveData<List<Pregunta>> preguntasLiveData = new MutableLiveData<>(new ArrayList<>()); // Lista vacía inicial
    // Estado actual del examen
    private IntentoExamen intentoExamen;
    private List<Pregunta> preguntas =new ArrayList<>();
    private List<RespuestaUsuario> respuestas= new ArrayList<>();
    private List<Opcion> opciones= new ArrayList<>();

    public IntentoViewModel() {
        this.intentoExamenRepository = new IntentoExamenRepository();
        this.preguntaRepository = new PreguntaRepository();
        this.respuestaUsuarioRepository = new RespuestaUsuarioRepository();
        this.opcionRepository = new OpcionRepository();
    }

    //conseguir para los livedata
    public LiveData<List<Pregunta>> getPreguntasLiveData() {
        return preguntasLiveData;
    }
    public LiveData<Pregunta> getPreguntaActualLiveData() {
        return preguntaActualLiveData;
    }
    public LiveData<List<Opcion>> getOpcionesLiveData() {
        return opcionesLiveData;
    }
    public LiveData<IntentoExamen> getIntentoExamenLiveData() {
        return intentoExamenLiveData;
    }
    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Integer> getPreguntaIndex() {
        return preguntaIndex;
    }

    public void iniciarTiempoExamen(int duracionEnMinutos) {
        tiempoRestanteEnMilis = duracionEnMinutos * 60 * 1000; // Convertir minutos a milisegundos
    }

    public long getTiempoRestanteEnMilis() {
        return tiempoRestanteEnMilis;
    }

    // metodos del viewmodel, son implementados en la activity

    public void iniciarIntentoExamen(int examenId, Integer usuarioId) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        IntentoExamen intentoExamenLocal = new IntentoExamen(examenId, usuarioId != null && usuarioId != -1 ? usuarioId : null);
        intentoExamenLocal.setFechaInicio(new Date());

        intentoExamenRepository.createIntentoExamen(intentoExamenLocal, new Callback<IntentoExamen>() {
            @Override
            public void onResponse(Call<IntentoExamen> call, Response<IntentoExamen> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    intentoExamen = response.body(); // Asignar al campo de la clase
                    intentoExamenLiveData.setValue(intentoExamen);
                    cargarPreguntas(examenId);
                } else {
                    errorMessage.setValue("Error al iniciar el examen: " + response.code());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("IntentoExamen", "Error response: " + errorBody);
                    } catch (Exception e) {
                        Log.e("IntentoExamen", "Error al leer el cuerpo de la respuesta", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<IntentoExamen> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void cargarPreguntas(int examenId) {
        isLoading.setValue(true);
        preguntaRepository.getPreguntasByExamen(examenId, new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    preguntas = response.body();
                    if (preguntas != null && !preguntas.isEmpty()) {
                        preguntasLiveData.setValue(preguntas);
                        preguntaIndex.setValue(0);
                        preguntaActualLiveData.setValue(preguntas.get(0));
                        cargarOpciones(preguntas.get(0).getId());
                    } else {
                        errorMessage.setValue("No hay preguntas disponibles");
                    }
                } else {
                    errorMessage.setValue("Error al cargar preguntas: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void guardarSeleccionOpcion(int preguntaId, int opcionId) {
        Map<Integer, Integer> respuestas = respuestasSeleccionadas.getValue();
        if (respuestas == null) {
            respuestas = new HashMap<>();
        }
        respuestas.put(preguntaId, opcionId);
        respuestasSeleccionadas.setValue(respuestas);


        List<Opcion> opciones = opcionesLiveData.getValue();
        if (opciones != null) {
            for (Opcion opcion : opciones) {
                if (opcion.getId() == opcionId) {
                    opcion.setSelected(true);
                } else {
                    opcion.setSelected(false);
                }
            }
            opcionesLiveData.setValue(opciones);
        }
    }

    public boolean hayOpcionSeleccionada(int preguntaId) {
        Map<Integer, Integer> respuestas = respuestasSeleccionadas.getValue();
        return respuestas != null && respuestas.containsKey(preguntaId);
    }


    public Integer getOpcionSeleccionada(int preguntaId) {
        Map<Integer, Integer> respuestas = respuestasSeleccionadas.getValue();
        return respuestas != null ? respuestas.get(preguntaId) : null;
    }

    private void cargarOpciones(int preguntaId) {
        isLoading.setValue(true);
        opcionRepository.getOpcionesByPregunta(preguntaId, new Callback<List<Opcion>>() {
            @Override
            public void onResponse(Call<List<Opcion>> call, Response<List<Opcion>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    List<Opcion> opciones = response.body();
                    if (opciones != null) {
                        // Se obtiene la opcion que se había elegido
                        Integer opcionSeleccionadaId = getOpcionSeleccionada(preguntaId);
                        if (opcionSeleccionadaId != null) {
                            for (Opcion opcion : opciones) {
                                if (opcion.getId() == opcionSeleccionadaId) {
                                    opcion.setSelected(true);
                                    break;
                                }
                            }
                        }
                        opcionesLiveData.setValue(opciones);
                    }
                } else {
                    errorMessage.setValue("Error al cargar opciones: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Opcion>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }


    public void siguientePregunta(){
        if (preguntaIndex.getValue() == null || preguntas.isEmpty()) {
            return;
        }
        int currentIndex = preguntaIndex.getValue();
        if(currentIndex < preguntas.size()-1){
            preguntaIndex.setValue(currentIndex + 1);
            preguntaActualLiveData.setValue(preguntas.get(currentIndex+1));
            cargarOpciones(preguntas.get(currentIndex + 1).getId());

        }
    }

    public void preguntaAnterior(){
        if (preguntaIndex.getValue() == null || preguntas.isEmpty()) {
            return;
        }
        int currentIndex = preguntaIndex.getValue();
        if(currentIndex > 0){
            preguntaIndex.setValue(currentIndex - 1);
            preguntaActualLiveData.setValue(preguntas.get(currentIndex - 1));
            cargarOpciones(preguntas.get(currentIndex - 1).getId());
        }
    }

    public void guardarRespuesta(int preguntaId, int opcionId, String respuestaTexto) {
        if (intentoExamen == null) {
            errorMessage.setValue("No se ha inicializado el intento");
            return;
        }

        RespuestaUsuario respuesta = new RespuestaUsuario(
                intentoExamen.getId(),
                preguntaId,
                opcionId,
                respuestaTexto
        );

        respuestaUsuarioRepository.createRespuestaUsuario(respuesta, new Callback<RespuestaUsuario>() {
            @Override
            public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                if (response.isSuccessful()) {
                    Log.d("IntentoViewModel", "Respuesta guardada correctamente");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("IntentoViewModel", "Error al guardar respuesta: " + errorBody);
                        errorMessage.setValue("Error al guardar respuesta: " + errorBody);
                    } catch (Exception e) {
                        Log.e("IntentoViewModel", "Error al leer el cuerpo de la respuesta", e);
                        errorMessage.setValue("Error al guardar respuesta");
                    }
                }
            }

            @Override
            public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                Log.e("IntentoViewModel", "Error de conexión al guardar respuesta: " + t.getMessage());
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void finalizarExamen() {
        if (intentoExamen == null) {
            errorMessage.setValue("No se ha inicializado el intento");
            return;
        }

        intentoExamen.setFechaFin(new Date());

        intentoExamenRepository.updateIntentoExamen(intentoExamen, new Callback<IntentoExamen>() {
            @Override
            public void onResponse(Call<IntentoExamen> call, Response<IntentoExamen> response) {
                if (response.isSuccessful()) {
                    intentoExamen = response.body();
                    intentoExamenLiveData.setValue(intentoExamen);
                    Log.d("IntentoViewModel", "Examen finalizado correctamente");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("IntentoViewModel", "Error al finalizar el examen: " + errorBody);
                        errorMessage.setValue("Error al finalizar el examen: " + errorBody);
                    } catch (Exception e) {
                        Log.e("IntentoViewModel", "Error al leer el cuerpo de la respuesta", e);
                        errorMessage.setValue("Error al finalizar el examen");
                    }
                }
            }

            @Override
            public void onFailure(Call<IntentoExamen> call, Throwable t) {
                Log.e("IntentoViewModel", "Error de conexión al finalizar el examen: " + t.getMessage());
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void cancelarExamen(){
        if(intentoExamen == null){
            errorMessage.setValue("No se ha inicializado el intento");
            return;
        }
        intentoExamenRepository.deleteIntentoExamen(intentoExamen.getId(), new Callback<IntentoExamen>() {
            @Override
            public void onResponse(Call<IntentoExamen> call, Response<IntentoExamen> response) {
                if (response.isSuccessful()) {
                    Log.d("IntentoViewModel", "Intento borrado.");
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("IntentoViewModel", "Error al borrar el intento: " + errorBody);
                        errorMessage.setValue("Error al borrar el intento: " + errorBody);
                    } catch (Exception e) {
                        Log.e("IntentoViewModel", "Error al leer el cuerpo de la respuesta", e);
                        errorMessage.setValue("Error al borrar el intento");
                    }
                }
            }


            @Override
            public void onFailure(Call<IntentoExamen> call, Throwable t) {
                Log.e("IntentoViewModel", "Error de conexión al finalizar el examen: " + t.getMessage());
                errorMessage.setValue("Error de conexión: " + t.getMessage());

        }
        });
    }


}
