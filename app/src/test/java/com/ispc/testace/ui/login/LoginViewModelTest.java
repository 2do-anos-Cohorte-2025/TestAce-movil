package com.ispc.testace.ui.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ispc.testace.data.model.AuthResponse;
import com.ispc.testace.data.repository.AuthRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTest {

    private static final String EMAIL = "alumno@test.com";
    private static final String PASSWORD = "clave1234";

    // Hace que LiveData funcione en la JVM, sin Android
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AuthRepository authRepository;

    @Mock
    private Call<AuthResponse> call;

    @Captor
    private ArgumentCaptor<Callback<AuthResponse>> callbackCaptor;

    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new LoginViewModel(authRepository);
    }

    // Atrapa el callback que el ViewModel le pasó al repositorio,
    // para simular desde el test la respuesta del servidor.
    private Callback<AuthResponse> capturarCallback() {
        verify(authRepository).login(anyString(), anyString(), callbackCaptor.capture());
        return callbackCaptor.getValue();
    }

    private ResponseBody cuerpoVacio() {
        return ResponseBody.create(MediaType.parse("application/json"), "");
    }

    @Test
    public void loginConCredencialesValidas_loginExitosoEsTrue() {
        // Arrange
        AuthResponse respuestaOk = mock(AuthResponse.class);

        // Act
        viewModel.login(EMAIL, PASSWORD);
        capturarCallback().onResponse(call, Response.success(respuestaOk));

        // Assert
        assertEquals(Boolean.TRUE, viewModel.getLoginExitoso().getValue());
        assertEquals(Boolean.FALSE, viewModel.isLoading().getValue());
        assertNull(viewModel.getErrorMessage().getValue());
        verify(authRepository, times(1)).login(eq(EMAIL), eq(PASSWORD), any());
    }

    @Test
    public void loginConCredencialesIncorrectas401_muestraMensajeEmailOContrasenaIncorrectos() {
        // Arrange
        Response<AuthResponse> respuesta401 = Response.error(401, cuerpoVacio());

        // Act
        viewModel.login(EMAIL, "claveEquivocada");
        capturarCallback().onResponse(call, respuesta401);

        // Assert
        assertEquals("Email o contraseña incorrectos", viewModel.getErrorMessage().getValue());
        assertEquals(Boolean.FALSE, viewModel.getLoginExitoso().getValue());
        assertEquals(Boolean.FALSE, viewModel.isLoading().getValue());
        verifyNoMoreInteractions(authRepository);
    }

    @Test
    public void loginSinConexionAlServidor_muestraMensajeNoSePudoConectar() {
        // Arrange
        Throwable errorDeRed = new IOException("timeout");

        // Act
        viewModel.login(EMAIL, PASSWORD);
        capturarCallback().onFailure(call, errorDeRed);

        // Assert
        assertEquals("No se pudo conectar con el servidor", viewModel.getErrorMessage().getValue());
        assertEquals(Boolean.FALSE, viewModel.getLoginExitoso().getValue());
        assertEquals(Boolean.FALSE, viewModel.isLoading().getValue());
        verifyNoMoreInteractions(authRepository);
    }
}