package com.ispc.testace.ui.examenes.catalogo;

import java.util.Locale;

public class FiltrosCatalogo {
    private String categoria;
    private String nivel;
    private String creador;

    public String getCategoria() { return categoria; }
    public String getNivel() { return nivel; }
    public String getCreador() { return creador; }

    /** null significa "Todas": no se envía al servidor. */
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public void setNivel(String nivel) { this.nivel = nivel; }

    /** Django espera 'profesor' o 'estudiante' en minúscula. */
    public void setCreador(String creador) {
        this.creador = creador != null ? creador.toLowerCase(Locale.ROOT) : null;
    }

    public boolean hayFiltrosActivos() {
        return categoria != null || nivel != null || creador != null;
    }

    public void limpiar() {
        categoria = null;
        nivel = null;
        creador = null;
    }
}