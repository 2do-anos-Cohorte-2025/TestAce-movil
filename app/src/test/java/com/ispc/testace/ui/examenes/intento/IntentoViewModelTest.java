package com.ispc.testace.ui.examenes.intento;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.junit.Rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.ispc.testace.data.model.Pregunta;

import java.util.List;

public class IntentoViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private IntentoViewModel intentoViewModel;

    @Before
    public void setUp() {
        // Arrange previo: Inicializamos la unidad bajo prueba (SUT)
        intentoViewModel = new IntentoViewModel();
    }

    @Test
    public void preguntaActualIndex_alIniciarViewModel_comienzaEnCero() {
        // Act
        Integer indiceActual = intentoViewModel.getPreguntaActualIndex().getValue();

        // Assert
        assertEquals("El índice de la pregunta actual debe comenzar siempre en la posición 0", Integer.valueOf(0), indiceActual);
    }

    @Test
    public void preguntasLiveData_alIniciarViewModel_noEsNulo() {
        // Act
        List<Pregunta> listaPreguntas = intentoViewModel.getPreguntasLiveData().getValue();

        // Assert
        assertEquals("La lista de preguntas inicial no debe ser nula", 0, listaPreguntas.size());
    }

    @Test
    public void hayRespuesta_conTipoOpcionMultipleSinSeleccion_retornaFalse() {
        // Arrange
        int preguntaId = 99;
        String tipo = "opcion_multiple";

        // Act
        boolean resultado = intentoViewModel.hayRespuesta(preguntaId, tipo);

        // Assert
        assertFalse("Una pregunta de tipo opcion multiple sin marcar opción no debe considerarse respondida", resultado);
    }

    @Test
    public void hayRespuestaTexto_conTextoVacio_retornaFalse() {
        // Arrange
        int preguntaId = 1;

        // Act
        boolean resultado = intentoViewModel.hayRespuestaTexto(preguntaId);

        // Assert
        assertFalse("Un texto vacio o no ingresado no es una respuesta válida", resultado);
    }


    @Test
    public void finalizarExamen_conIntentoNulo_estableceMensajeError() {
        // Arrange: intentoExamen permanece null por defecto

        // Act: Intentamos finalizar la prueba
        intentoViewModel.finalizarExamen();

        // Assert: Verificamos que el LiveData de error registre el mensaje esperado
        assertEquals("No se ha inicializado el intento", intentoViewModel.getErrorMessage().getValue());
    }


    @Test
    public void errorMessage_alIniciarViewModel_esNulo() {
        // Act
        String mensajeError = intentoViewModel.getErrorMessage().getValue();

        // Assert
        assertEquals("Al iniciar el ViewModel de Intento no debería existir ningún mensaje de error previo", null, mensajeError);
    }
}