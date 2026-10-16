package com.ispc.testace.ui.examenes.modificar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ispc.testace.data.model.Examen;
import com.ispc.testace.data.repository.ExamenRepository;
import com.ispc.testace.data.repository.OpcionRepository;
import com.ispc.testace.data.repository.PreguntaRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarExamenViewModelTest {

    // Hace que LiveData funcione en la JVM (sin el hilo principal de Android)
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ExamenRepository examenRepository;
    @Mock
    private PreguntaRepository preguntaRepository;
    @Mock
    private OpcionRepository opcionRepository;
    @Mock
    private Call<Examen> call;

    @Captor
    private ArgumentCaptor<Callback<Examen>> callbackCaptor;
    @Captor
    private ArgumentCaptor<Map<String, Object>> datosCaptor;

    private ModificarExamenViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new ModificarExamenViewModel(
                examenRepository, preguntaRepository, opcionRepository);
    }

    // ---------- Caso feliz ----------

    @Test
    public void guardarExamen_conRespuestaExitosa_publicaMensajeDeGuardadoYActualizaExamen() {
        // Arrange
        Examen examenGuardado = new Examen();
        Response<Examen> respuestaOk = Response.success(examenGuardado);

        // Act
        viewModel.guardarExamen("examen-java", "Java Basico", "Introduccion a Java");
        verify(examenRepository).updateExamen(eq("examen-java"), any(), callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, respuestaOk);

        // Assert
        assertEquals("Datos del examen guardados.", viewModel.getMensaje().getValue());
        assertSame(examenGuardado, viewModel.getExamen().getValue());
    }

    @Test
    public void guardarExamen_conTituloYDescripcion_enviaEsosDatosAlRepositorio() {
        // Arrange
        String slug = "examen-java";

        // Act
        viewModel.guardarExamen(slug, "Java Basico", "Introduccion a Java");

        // Assert
        verify(examenRepository).updateExamen(eq(slug), datosCaptor.capture(), any());
        assertEquals("Java Basico", datosCaptor.getValue().get("titulo"));
        assertEquals("Introduccion a Java", datosCaptor.getValue().get("descripcion"));
    }

    // ---------- Casos de error ----------

    @Test
    public void guardarExamen_conRespuesta500_publicaMensajeDeErrorConCodigo() {
        // Arrange
        Response<Examen> respuesta500 = Response.error(500,
                ResponseBody.create("", MediaType.parse("application/json")));

        // Act
        viewModel.guardarExamen("examen-java", "Java Basico", "Introduccion a Java");
        verify(examenRepository).updateExamen(eq("examen-java"), any(), callbackCaptor.capture());
        callbackCaptor.getValue().onResponse(call, respuesta500);

        // Assert
        assertEquals("Error al guardar el examen. Código: 500", viewModel.getMensaje().getValue());
        assertNull(viewModel.getExamen().getValue());
    }

    @Test
    public void guardarExamen_conFalloDeRed_publicaMensajeDeErrorDeConexion() {
        // Arrange
        IOException falloDeRed = new IOException("sin conexion");

        // Act
        viewModel.guardarExamen("examen-java", "Java Basico", "Introduccion a Java");
        verify(examenRepository).updateExamen(eq("examen-java"), any(), callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, falloDeRed);

        // Assert
        assertEquals("Error de conexión al guardar el examen.", viewModel.getMensaje().getValue());
    }

    @Test
    public void cargarExamen_conFalloDeRed_apagaCargandoYPublicaError() {
        // Arrange
        IOException falloDeRed = new IOException("timeout");

        // Act
        viewModel.cargarExamen("examen-java");
        verify(examenRepository).getExamenBySlug(eq("examen-java"), callbackCaptor.capture());
        callbackCaptor.getValue().onFailure(call, falloDeRed);

        // Assert
        assertEquals(Boolean.FALSE, viewModel.getCargando().getValue());
        assertEquals("Error de conexión: timeout", viewModel.getMensaje().getValue());
    }

    // ---------- Sin interacciones ----------

    @Test
    public void viewModelRecienCreado_noInvocaNingunRepositorio() {
        // Arrange (el ViewModel ya se creó en setUp, sin llamar a ningún método)

        // Act
        Boolean cargando = viewModel.getCargando().getValue();

        // Assert
        assertEquals(Boolean.FALSE, cargando);
        assertNull(viewModel.getMensaje().getValue());
        verifyNoInteractions(examenRepository, preguntaRepository, opcionRepository);
    }
}