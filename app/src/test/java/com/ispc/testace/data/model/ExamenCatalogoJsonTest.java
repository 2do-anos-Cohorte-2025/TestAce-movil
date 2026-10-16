package com.ispc.testace.data.model;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.List;

public class ExamenCatalogoJsonTest {

    private final Gson gson = new Gson();

    @Test
    public void fromJson_examenCompleto_mapeaLosCampos() {
        // Arrange
        String json = "{\"id\":2,\"usuario_nombre\":\"estudiante1 prueba\","
                + "\"categoria_nombre\":\"Medio\",\"nivel_nombre\":\"avanzado\","
                + "\"es_profesor\":false,\"titulo\":\"PRUEBA1\",\"slug\":\"prueba1\","
                + "\"descripcion\":\"PRUEBA\",\"tiempo_limite\":40,\"imagen_examen\":null}";
        // Act
        ExamenCatalogo e = gson.fromJson(json, ExamenCatalogo.class);
        // Assert
        assertEquals(2, e.getId());
        assertEquals("PRUEBA1", e.getTitulo());
        assertEquals("prueba1", e.getSlug());
        assertEquals("Medio", e.getCategoriaNombre());
        assertEquals("avanzado", e.getNivelNombre());
        assertEquals(Integer.valueOf(40), e.getTiempoLimite());
        assertFalse(e.isEsProfesor());
        assertNull(e.getImagenExamen());
    }

    @Test
    public void fromJson_esProfesorTrue_seInterpretaComoProfesor() {
        String json = "{\"id\":1,\"titulo\":\"X\",\"es_profesor\":true}";
        ExamenCatalogo e = gson.fromJson(json, ExamenCatalogo.class);
        assertTrue(e.isEsProfesor());
    }

    @Test
    public void fromJson_sinTiempoLimite_quedaNull() {
        String json = "{\"id\":1,\"titulo\":\"X\",\"tiempo_limite\":null}";
        ExamenCatalogo e = gson.fromJson(json, ExamenCatalogo.class);
        assertNull(e.getTiempoLimite());
    }

    @Test
    public void fromJson_listaVacia_devuelveListaSinElementos() {
        List<ExamenCatalogo> lista = gson.fromJson("[]",
                new TypeToken<List<ExamenCatalogo>>() { }.getType());
        assertNotNull(lista);
        assertTrue(lista.isEmpty());
    }
}