package com.ispc.testace.data.model;

public class RespuestaUsuario {
    private int id;
    private int intento;
    private int pregunta;
    private Integer opcionSeleccionada;
    private String respuestaTexto;

    public RespuestaUsuario(int intento,int pregunta, Integer opcionSeleccionada, String respuestaTexto ) {
        this.intento = intento;
        this.pregunta = pregunta;
        this.opcionSeleccionada = opcionSeleccionada;
        this.respuestaTexto =respuestaTexto;
    }



    // Borrar por inutilidad
    public void registrarRespuesta(int opcion) {
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

    public int getIntento() {
        return intento;
    }

    public void setIntento(int intento) {
        this.intento = intento;
    }

    public Integer getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public void setOpcionSeleccionada(Integer opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    public int getPregunta() {
        return pregunta;
    }

    public void setPregunta(int pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }
    public void setRespuestaTexto(String respuestaTexto){
        this.respuestaTexto=respuestaTexto;
    }

}
