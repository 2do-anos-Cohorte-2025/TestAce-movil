package com.ispc.testace.ui.examenes.catalogo;

public class TarjetaExamenFormatter {

    public static String categoria(String categoria) {
        return "Categoría: " + (categoria != null ? categoria : "Sin categoría");
    }

    public static String nivel(String nivel) {
        return "Nivel: " + (nivel != null ? nivel : "Sin nivel");
    }

    public static String tiempoLimite(Integer minutos) {
        return minutos != null ? "Tiempo límite: " + minutos + " min" : "Sin tiempo límite";
    }
}