package com.ispc.testace.ui.examenes.intento;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ispc.testace.data.model.IntentoExamen;
import com.ispc.testace.data.model.Pregunta;
import com.ispc.testace.data.model.Opcion;
import com.ispc.testace.data.model.RespuestaUsuario;
import com.ispc.testace.data.repository.IntentoExamenRepository;
import com.ispc.testace.data.repository.OpcionRepository;
import com.ispc.testace.data.repository.PreguntaRepository;
import com.ispc.testace.data.repository.RespuestaUsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class IntentoViewModel extends ViewModel{
    private  IntentoExamenRepository intentoExamenRepository;
    private  PreguntaRepository preguntaRepository;
    private RespuestaUsuarioRepository respuestaUsuarioRepository;

    private OpcionRepository opcionRepository;

    //Datos observables
    private final MutableLiveData<List<Pregunta>> preguntasLiveData = new MutableLiveData<>();
    private final MutableLiveData<Pregunta> preguntaActualLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Opcion>> opcionesLiveData = new MutableLiveData<>();
    private final MutableLiveData<IntentoExamen> intentoExamenLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final MutableLiveData<Integer> preguntaIndex = new MutableLiveData<>();


    // Estado actual del examen
    private IntentoExamen intentoExamen;
    private List<Pregunta> preguntas =new ArrayList<>();
    private List<RespuestaUsuario> respuestas= new ArrayList<>();
    private List<Opcion> opciones= new ArrayList<>();

    public void IntentoExamenViewModel() {
        this.intentoExamenRepository = new IntentoExamenRepository();
        this.preguntaRepository = new PreguntaRepository();
        this.respuestaUsuarioRepository = new RespuestaUsuarioRepository();
        this.opcionRepository= new OpcionRepository();
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


    // metodos del viewmodel

    public void iniciarIntentoExamen(int examenId, int usuarioId){
        isLoading.setValue(true);
        errorMessage.setValue(null);

        intentoExamen= new IntentoExamen(examenId, usuarioId);
        intentoExamen.setFechaInicio(new Date());

        intentoExamenRepository.createIntentoExamen(intentoExamen, new Callback<IntentoExamen>() {
            @Override
            public void onResponse(Call<IntentoExamen> call, Response<IntentoExamen> response) {
                isLoading.setValue(false);
                if(response.isSuccessful()){
                    intentoExamen =response.body();
                    intentoExamenLiveData.setValue(intentoExamen);
                    cargarPreguntas(examenId);
                }
                else{
                    errorMessage.setValue("Error al iniciar el examen: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<IntentoExamen> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexion: " + t.getMessage());
            }
        });

    }

    private void cargarPreguntas(int examenId){
        isLoading.setValue(true);
        preguntaRepository.getPreguntasByExamen(examenId, new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                isLoading.setValue(false);
                if(response.isSuccessful()){
                    preguntas =response.body();
                    preguntasLiveData.setValue(preguntas);
                    if(!preguntas.isEmpty()){
                        preguntaIndex.setValue(0);
                        preguntaActualLiveData.setValue(preguntas.get(0));
                        cargarOpciones(preguntas.get(0).getId());
                    }
                    else{
                        errorMessage.setValue("Error al cargar preguntas: " + response.code());
                    }}}
            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void cargarOpciones(int preguntaId){
        isLoading.setValue(true);
        opcionRepository.getOpcionesByPregunta(preguntaId, new Callback<List<Opcion>>() {
            @Override
            public void onResponse(Call<List<Opcion>> call, Response<List<Opcion>> response) {
                isLoading.setValue(false);
                if(response.isSuccessful()){
                    opciones = response.body();
                    if(!opciones.isEmpty()){
                        opcionesLiveData.setValue(opciones);
                    }
                    else{
                        errorMessage.setValue("Error al cargar opciones: " + response.code());
                    }}}
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

    public void guardarRespuesta(int preguntaId, int opcionSeleccionada,String respuestaTexto){
        RespuestaUsuario respuesta = new RespuestaUsuario(
                intentoExamen.getId(),
                preguntaId,
                opcionSeleccionada,
                respuestaTexto
        );
        respuestaUsuarioRepository.createRespuestaUsuario(respuesta, new Callback<RespuestaUsuario>() {
            @Override
            public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                if(!response.isSuccessful()){
                    errorMessage.setValue("Error al guardar la respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                errorMessage.setValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void finalizarExamen(){
        if(intentoExamen == null ) return;
        intentoExamen.setFechaFin(new Date());

        intentoExamen.setResultado(calcularResultado());

        intentoExamenRepository.updateIntentoExamen(
                intentoExamen.getId(),
                intentoExamen,
                new Callback<IntentoExamen>() {
                    @Override
                    public void onResponse(Call<IntentoExamen> call, Response<IntentoExamen> response) {
                        if(response.isSuccessful()){
                            intentoExamen= response.body();
                            intentoExamenLiveData.setValue(intentoExamen);
                        }else{
                            errorMessage.setValue("Error al finalizar el examen: " +response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<IntentoExamen> call, Throwable t) {
                        errorMessage.setValue("Error de conexión: " + t.getMessage());
                    }
                }
        );
    }


    // Hacer calculo
    public double calcularResultado(){
        return 0.0;
    }




}
