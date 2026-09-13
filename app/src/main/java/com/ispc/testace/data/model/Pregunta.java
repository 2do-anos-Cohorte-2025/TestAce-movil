package com.ispc.testace.data.model;

import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private int id;
    private String enunciado;
    private String tipo;
    private double puntos;
    private String imagenPregunta;
    private List<Opcion> opciones;

    public Pregunta(int id, String enunciado, String tipo, double puntos, String imagenPregunta) {
        this.id = id;
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.puntos = puntos;
        this.imagenPregunta = imagenPregunta;
        this.opciones = new ArrayList<>();
    }

    public void agregarOpcion(Opcion opcion) {
        opciones.add(opcion);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEnunciado() {
        return enunciado;
    }
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPuntos() {
        return puntos;
    }
    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public String getImagenPregunta() {
        return imagenPregunta;
    }
    public void setImagenPregunta(String imagenPregunta) {
        this.imagenPregunta = imagenPregunta;
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }
    public void setOpciones(List<Opcion> opciones) {
        this.opciones = opciones;
    }
}
