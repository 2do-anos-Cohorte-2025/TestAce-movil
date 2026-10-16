package com.ispc.testace.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private int id;
    @SerializedName("examen")
    private int examen;
    private String enunciado;
    private String tipo;
    private boolean permitir_varias_respuestas = false;
    private double puntos;
    private String imagen_pregunta;
    private List<Opcion> opciones;

    public Pregunta() {
        this.opciones = new ArrayList<>();
    }

    public Pregunta(int id, String enunciado, String tipo, double puntos, String imagen_pregunta) {
        this.id = id;
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.puntos = puntos;
        this.imagen_pregunta = imagen_pregunta;
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

    public int getExamen() {
        return examen;
    }

    public void setExamen(int examen) {
        this.examen = examen;
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

    public boolean isPermitir_varias_respuestas() {
        return permitir_varias_respuestas;
    }
    public void setPermitir_varias_respuestas(boolean permitir_varias_respuestas) {
        this.permitir_varias_respuestas = permitir_varias_respuestas;
    }

    public double getPuntos() {
        return puntos;
    }
    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }

    public String getImagen_pregunta() {
        return imagen_pregunta;
    }
    public void setImagen_pregunta(String imagen_pregunta) {
        this.imagen_pregunta = imagen_pregunta;
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }
    public void setOpciones(List<Opcion> opciones) {
        this.opciones = opciones;
    }
}
