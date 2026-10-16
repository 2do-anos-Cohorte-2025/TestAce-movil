package com.ispc.testace.ui.examenes.catalogo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarjetaExamenFormatterTest {
    @Test
    public void categoria_conValor_muestraElNombre() {
        // Arrange + Act: el examen tiene la categoría "Matemática"
        String texto = TarjetaExamenFormatter.categoria("Matemática");

        // Assert: la tarjeta muestra la etiqueta seguida del nombre
        assertEquals("Debe mostrar la categoría con su etiqueta",
                "Categoría: Matemática", texto);
    }

    @Test
    public void categoria_null_muestraSinCategoria() {
        // Arrange + Act: el examen no tiene categoría asignada (null)
        String texto = TarjetaExamenFormatter.categoria(null);

        // Assert: se muestra un texto alternativo en lugar de "null" o cerrar la app
        assertEquals("Sin categoría debe mostrar el texto alternativo",
                "Categoría: Sin categoría", texto);
    }

    @Test
    public void nivel_conValor_muestraElNombre() {
        // Arrange + Act: el examen tiene el nivel "Alto"
        String texto = TarjetaExamenFormatter.nivel("Alto");

        // Assert: la tarjeta muestra la etiqueta seguida del nivel
        assertEquals("Debe mostrar el nivel con su etiqueta", "Nivel: Alto", texto);
    }


    }