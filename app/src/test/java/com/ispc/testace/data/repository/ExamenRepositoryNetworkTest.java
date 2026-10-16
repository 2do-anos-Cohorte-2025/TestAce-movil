package com.ispc.testace.data.repository;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ispc.testace.data.model.ExamenCatalogo;
import com.ispc.testace.data.service.ApiService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExamenRepositoryNetworkTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MockWebServer mockWebServer;
    private ApiService apiService;

    @Before
    public void setUp() throws IOException {
        // AUT-NET-01: Arrancamos el servidor local simulado con MockWebServer

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Se crea un Retrofit apuntando a la URL de MockWebServer
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    // Se finaliza el servidor local al terminar cada prueba
    @After
    public void tearDown() throws IOException {

        mockWebServer.shutdown();
    }


    // AUT-NET-02 (Caso A): Respuesta HTTP 200 OK, nos permite deserializar correctamente el modelo de examen

    @Test
    public void getExamenesCatalogo_conRespuesta200_convierteJsonCorrectamenteEnObjetos() throws Exception {
        // Arrange:
        String jsonResponse = "[" +
                "  {" +
                "    \"id\": 2," +
                "    \"titulo\": \"Ecología Básica\"," +
                "    \"slug\": \"ecologia-basica\"," +
                "    \"categoria\": \"Ciencias\"," +
                "    \"nivel\": \"Dificil\"" +
                "  }" +
                "]";

        // Se otorga la respuesta HTTP 200 en el servidor Mock creado
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse));

        // Act: Consumimos el endpoint usando el ApiService conectado a MockWebServer
        Call<List<ExamenCatalogo>> call = apiService.getExamenesCatalogo(null, null, null, null);
        Response<List<ExamenCatalogo>> response = call.execute();

        // Assert: Verificamos que se convierta bien el JSON en objetos de la app
        assertTrue("La respuesta HTTP debe ser exitosa (200 OK)", response.isSuccessful());
        assertNotNull("El cuerpo de la respuesta no puede ser nulo", response.body());
        assertEquals("Debe deserializar exactamente una instancia", 1, response.body().size());
        assertEquals("El título del examen debe coincidir", "Ecología Básica", response.body().get(0).getTitulo());
        assertEquals("El slug debe coincidir", "ecologia-basica", response.body().get(0).getSlug());
    }


    // AUT-NET-02 (Caso B): Respuesta HTTP 500 Server Error, nos permite manejar las excepciones por la respuesta 500

    @Test
    public void getExamenesCatalogo_conRespuesta500_manejaErrorSinRomperse() throws Exception {
        // Arrange: Forzando un fallo interno del servidor con el codigo 500 (HTTP)
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        // Act: Se ejecuta la llamada a la red simulada
        Call<List<ExamenCatalogo>> call = apiService.getExamenesCatalogo(null, null, null, null);
        Response<List<ExamenCatalogo>> response = call.execute();

        // Assert: Se detecta el código 500 y la aplicación no se detiene
        assertTrue("La respuesta no debe ser exitosa ante un error de servidor", !response.isSuccessful());
        assertEquals("El codigo de error HTTP de la respuesta debe ser 500", 500, response.code());
    }
}