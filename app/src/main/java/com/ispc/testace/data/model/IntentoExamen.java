package com.ispc.testace.data.model;

import java.util.Date;

public class IntentoExamen {
    private int id;
    private Examen examen;
    private Integer usuario; //Se debe remplazar por la clase Usuario
    private Date fechaInicio;
    private Date fechaFin;
    private Double resultado;

    public IntentoExamen(int id, Examen examen, Integer usuario) {
        this.id = id;
        this.examen = examen;
        this.usuario = usuario;
        this.fechaInicio = new Date();
        this.fechaFin = null;
        this.resultado = null;
    }

    public void finalizarIntento(double resultado) {
        this.fechaFin = new Date();
        this.resultado = resultado;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Examen getExamen() {
        return examen;
    }
    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }
    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio){
        this.fechaInicio=fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getResultado() {
        return resultado;
    }
    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }
}
