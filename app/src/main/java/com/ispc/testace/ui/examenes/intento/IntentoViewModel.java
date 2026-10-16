package com.ispc.testace.ui.examenes.intento;


import android.util.Log;

import java.util.HashSet;
import java.util.Set;

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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
    private final MutableLiveData<IntentoExamen> intentoExamenLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Pregunta>> preguntasLiveData = new MutableLiveData<>(new ArrayList<>()); // Lista vacía inicial
    private final MutableLiveData<Pregunta> preguntaActualLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> preguntaActualIndex = new MutableLiveData<>(0);
    private final MutableLiveData<List<Opcion>> opcionesLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Map<Integer, Set<Integer>>> respuestasSeleccionadas = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Map<Integer, String>> respuestasTexto = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Double> resultadoLiveData = new MutableLiveData<>();
    private final MutableLiveData<Float> tiempoTranscurridoLiveData = new MutableLiveData<>();




    // Estado actual del examen
    private IntentoExamen intentoExamen;
    private List<Pregunta> preguntas =new ArrayList<>();
    /*private List<RespuestaUsuario> respuestas= new ArrayList<>();
    private List<Opcion> opciones= new ArrayList<>();*/

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
    public LiveData<Integer> getPreguntaActualIndex() {
        return preguntaActualIndex;
    }




    public void iniciarTiempoExamen(int duracionEnMinutos) {
        tiempoRestanteEnMilis = duracionEnMinutos * 60 * 1000; // Convertir minutos a milisegundos
    }

    public long getTiempoRestanteEnMilis() {
        return tiempoRestanteEnMilis;
    }

    public LiveData<Double> getResultadoLiveData() {
        return resultadoLiveData;
    }

    public LiveData<Float> getTiempoTranscurridoLiveData() {
        return tiempoTranscurridoLiveData;
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
        opcionesLiveData.setValue(new ArrayList<>());
        isLoading.setValue(true);
        preguntaRepository.getPreguntasByExamen(examenId, new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    preguntas = response.body();
                    if (preguntas != null && !preguntas.isEmpty()) {
                        preguntasLiveData.setValue(preguntas);
                        preguntaActualIndex.setValue(0);
                        preguntaActualLiveData.setValue(preguntas.get(0));
                        cargarOpciones(preguntas.get(0).getId());
                    } else {
                        preguntasLiveData.setValue(new ArrayList<>());
                        errorMessage.setValue("No hay preguntas disponibles");
                    }
                } else {
                    errorMessage.setValue("Error al cargar preguntas: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                preguntasLiveData.setValue(new ArrayList<>());
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void siguientePregunta(){
        if (preguntaActualIndex.getValue() == null || preguntas.isEmpty()) {
            return;
        }
        int currentIndex = preguntaActualIndex.getValue();
        if(currentIndex < preguntas.size()-1){
            int newIndex = currentIndex + 1;
            preguntaActualIndex.setValue(newIndex);
            preguntaActualLiveData.setValue(preguntas.get(newIndex));
            cargarOpciones(preguntas.get(newIndex).getId());

        }
    }

    public void preguntaAnterior(){
        if (preguntaActualIndex.getValue() == null || preguntas.isEmpty()) {
            return;
        }
        int currentIndex = preguntaActualIndex.getValue();
        if(currentIndex > 0){
            int newIndex = currentIndex - 1;
            preguntaActualIndex.setValue(newIndex);
            preguntaActualLiveData.setValue(preguntas.get(newIndex));
            cargarOpciones(preguntas.get(newIndex).getId());
        }
    }

    public void guardarSeleccionOpcion(int preguntaId, int opcionId) {
        Map<Integer, Set<Integer>> respuestas = respuestasSeleccionadas.getValue();

        if (respuestas == null) {
            respuestas = new HashMap<>();
        }

        Set<Integer> opcionesPregunta = respuestas.get(preguntaId);

        if (opcionesPregunta == null) {
            opcionesPregunta = new HashSet<>();
        }

        if (opcionesPregunta.contains(opcionId)) {
            opcionesPregunta.remove(opcionId);
        } else {
            opcionesPregunta.add(opcionId);
        }

        respuestas.put(preguntaId, opcionesPregunta);
        respuestasSeleccionadas.setValue(respuestas);

        List<Opcion> opciones = opcionesLiveData.getValue();

        if (opciones != null) {
            for (Opcion opcion : opciones) {
                opcion.setSelected(opcionesPregunta.contains(opcion.getId()));
            }

            opcionesLiveData.setValue(opciones);
        }
    }

    public boolean hayOpcionSeleccionada(int preguntaId) {
        Map<Integer, Set<Integer>> respuestas = respuestasSeleccionadas.getValue();

        if (respuestas == null || respuestas.get(preguntaId) == null) {
            return false;
        }

        return !respuestas.get(preguntaId).isEmpty();
    }

    public Set<Integer> getOpcionesSeleccionadas(int preguntaId) {
        Map<Integer, Set<Integer>> respuestas = respuestasSeleccionadas.getValue();

        if (respuestas == null || respuestas.get(preguntaId) == null) {
            return new HashSet<>();
        }

        return new HashSet<>(respuestas.get(preguntaId));
    }
    public void guardarRespuestaTexto(int preguntaId, String texto) {
        Map<Integer, String> respuestas = respuestasTexto.getValue();

        if (respuestas == null) {
            respuestas = new HashMap<>();
        }

        respuestas.put(preguntaId, texto);
        respuestasTexto.setValue(respuestas);
    }

    public String getRespuestaTexto(int preguntaId) {
        Map<Integer, String> respuestas = respuestasTexto.getValue();
        return respuestas != null ? respuestas.get(preguntaId) : null;
    }

    public boolean hayRespuestaTexto(int preguntaId) {
        String respuesta = getRespuestaTexto(preguntaId);
        return respuesta != null && !respuesta.trim().isEmpty();
    }

   // Prohibe la falta de una respuesta, debe ser seleccionada o escrita
    public boolean hayRespuesta(int preguntaId, String tipo) {
        if ("opcion_multiple".equals(tipo) || "verdadero_falso".equals(tipo)) {
            return hayOpcionSeleccionada(preguntaId);
        }

        if ("texto".equals(tipo) || "numerico".equals(tipo)) {
            return hayRespuestaTexto(preguntaId);
        }

        return false;
    }

    private void cargarOpciones(int preguntaId) {
        isLoading.setValue(true);
        opcionRepository.getOpcionesByPregunta(preguntaId, new Callback<List<Opcion>>() {
            @Override
            public void onResponse(Call<List<Opcion>> call, Response<List<Opcion>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()  && response.body() != null) {
                    List<Opcion> opciones = response.body();
                    opcionesLiveData.setValue(response.body());
                    if (opciones != null) {
                        // Se obtiene la opcion que se había elegido
                        Set<Integer> seleccionadas = getOpcionesSeleccionadas(preguntaId);

                        for (Opcion opcion : opciones) {
                            opcion.setSelected(seleccionadas.contains(opcion.getId()));
                        }

                        opcionesLiveData.setValue(opciones);
                    }
                } else {
                    opcionesLiveData.setValue(new ArrayList<>());
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


    public void guardarRespuesta(int preguntaId, Integer opcionId, String respuestaTexto) {
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
        Date fechaInicio = intentoExamen.getFechaInicio();
        Date fechaFin = new Date();

        if (fechaInicio != null) {

            float minutos = (fechaFin.getTime() - fechaInicio.getTime())
                    / 60000f;
            float minutosRedondeados = Math.round(minutos * 100) / 100f;
            tiempoTranscurridoLiveData.setValue(minutosRedondeados);
        }

        intentoExamenRepository.updateIntentoExamen(intentoExamen,new Callback<IntentoExamen>() {
            @Override
            public void onResponse(Call<IntentoExamen> call, Response<IntentoExamen> response) {

                if (response.isSuccessful() && response.body() != null) {
                    intentoExamen = response.body();
                    intentoExamenLiveData.setValue(intentoExamen);

                    Double resultado = intentoExamen.getResultado();
                    Float tiempo = intentoExamen.getTiempoTranscurrido();

                    resultadoLiveData.setValue(resultado);
                    tiempoTranscurridoLiveData.setValue(tiempo);

                    Log.d("IntentoViewModel", "Resultado: " + resultado);
                    Log.d("IntentoViewModel", "Tiempo: " + tiempo);
                        } else {
                            errorMessage.setValue(
                                    "Error al finalizar el examen: " + response.code()
                            );
                        }
                    }
                @Override
                public void onFailure(Call<IntentoExamen> call, Throwable t) {
                        errorMessage.setValue("Error de conexión: " + t.getMessage());
                    }
                }
        );
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
