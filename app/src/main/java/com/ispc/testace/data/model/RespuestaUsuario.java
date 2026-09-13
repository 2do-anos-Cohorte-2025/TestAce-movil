package com.ispc.testace.data.model;

public class RespuestaUsuario {
    private int id;
    private IntentoExamen intento;
    private Pregunta pregunta;
    private Opcion opcionSeleccionada;
    private String respuestaTexto;

    public RespuestaUsuario(int id, IntentoExamen intento, Pregunta pregunta) {
        this.id = id;
        this.intento = intento;
        this.pregunta = pregunta;
    }

    public void registrarRespuesta(Opcion opcion) {
        this.opcionSeleccionada = opcion;
        this.respuestaTexto = null;
    }

    public void registrarRespuesta(String texto) {
        this.respuestaTexto = texto;
        this.opcionSeleccionada = null;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public IntentoExamen getIntento() {
        return intento;
    }

    public void setIntento(IntentoExamen intento) {
        this.intento = intento;
    }

    public Opcion getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public void setOpcionSeleccionada(Opcion opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }
    public void setRespuestaTexto(String respuestaTexto){
        this.respuestaTexto=respuestaTexto;
    }

}
