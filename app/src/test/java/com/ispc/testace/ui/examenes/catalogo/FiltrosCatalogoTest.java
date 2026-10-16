package com.ispc.testace.ui.examenes.catalogo;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FiltrosCatalogoTest {

    private FiltrosCatalogo filtros;

    @Before
    public void setUp() {
        filtros = new FiltrosCatalogo();
    }

    @Test
    public void nuevo_sinFiltros_noHayFiltrosActivos() {
        // Arrange: objeto recién creado
        // Act
        boolean activos = filtros.hayFiltrosActivos();
        // Assert
        assertFalse("Un filtro recién creado no debe tener filtros activos", activos);
        assertNull("La categoría inicial debe ser null", filtros.getCategoria());
        assertNull("El nivel inicial debe ser null", filtros.getNivel());
        assertNull("El creador inicial debe ser null", filtros.getCreador());
    }

    @Test
    public void setCategoria_conValor_quedaGuardadaYActiva() {
        // Arrange
        String categoria = "Matemática";
        // Act
        filtros.setCategoria(categoria);
        // Assert
        assertEquals("La categoría debe quedar guardada", "Matemática", filtros.getCategoria());
        assertTrue("Con una categoría elegida debe haber filtros activos", filtros.hayFiltrosActivos());
    }

    @Test
    public void setCategoria_conNull_quitaElFiltro() {
        // Arrange
        filtros.setCategoria("Matemática");
        // Act
        filtros.setCategoria(null);
        // Assert
        assertNull("Al elegir 'Todas' la categoría debe quedar en null", filtros.getCategoria());
        assertFalse("Sin categoría elegida no debe haber filtros activos", filtros.hayFiltrosActivos());
    }

    @Test
    public void limpiar_conTodosLosFiltros_losDejaEnNull() {
        // Arrange
        filtros.setCategoria("Historia");
        filtros.setNivel("Alto");
        filtros.setCreador("Profesor");
        // Act
        filtros.limpiar();
        // Assert
        assertNull("Limpiar debe dejar la categoría en null", filtros.getCategoria());
        assertNull("Limpiar debe dejar el nivel en null", filtros.getNivel());
        assertNull("Limpiar debe dejar el creador en null", filtros.getCreador());
        assertFalse("Después de limpiar no debe haber filtros activos", filtros.hayFiltrosActivos());
    }
}

