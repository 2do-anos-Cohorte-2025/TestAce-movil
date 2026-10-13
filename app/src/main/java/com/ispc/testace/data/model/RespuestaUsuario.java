package com.ispc.testace.data.model;

public class RespuestaUsuario {
    private int id;
    private int intento;
    private int pregunta;
    private Integer opcion_seleccionada;
    private String respuesta_texto;

    public RespuestaUsuario() {}

    public RespuestaUsuario(int intento, int pregunta, Integer opcion_seleccionada, String respuesta_texto) {
        this.intento = intento;
        this.pregunta = pregunta;
        this.opcion_seleccionada = opcion_seleccionada;
        this.respuesta_texto = respuesta_texto;
    }


    // Borrar por inutilidad
    public void registrarRespuesta(int opcion) {
        this.opcion_seleccionada = opcion;
        this.respuesta_texto = null;
    }

    public void registrarRespuesta(String texto) {
        this.respuesta_texto = texto;
        this.opcion_seleccionada = null;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIntento() { return intento; }
    public void setIntento(int intento) { this.intento = intento; }

    public int getPregunta() { return pregunta; }
    public void setPregunta(int pregunta) { this.pregunta = pregunta; }

    public Integer getOpcion_seleccionada() { return opcion_seleccionada; }
    public void setOpcion_seleccionada(Integer opcion_seleccionada) { this.opcion_seleccionada = opcion_seleccionada; }

    public String getRespuesta_texto() { return respuesta_texto; }
    public void setRespuesta_texto(String respuesta_texto) { this.respuesta_texto = respuesta_texto; }

}
