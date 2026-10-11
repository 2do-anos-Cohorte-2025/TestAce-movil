package com.ispc.testace.data.model;

import java.util.Date;

public class IntentoExamen {
    private int id;
    private int examen;
    private String examen_titulo;
    private String examen_slug;
    private Integer usuario;
    private Date fecha_inicio;
    private Date fecha_fin;
    private Double resultado;

    public IntentoExamen(int examen, Integer usuario) {
        this.examen = examen;
        this.usuario = usuario;
        /*this.fechaInicio = new Date();
        this.fechaFin = null;
        this.resultado = null;*/
    }



    public void finalizarIntento(double resultado) {
        this.fecha_fin = new Date();
        this.resultado = resultado;
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

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String getExamen_titulo() {
        return examen_titulo;
    }

    public void setExamen_titulo(String examen_titulo) {
        this.examen_titulo = examen_titulo;
    }

    public String getExamen_slug() {
        return examen_slug;
    }

    public void setExamen_slug(String examen_slug) {
        this.examen_slug = examen_slug;
    }

    public Date getFechaInicio() {
        return fecha_inicio;
    }
    public void setFechaInicio(Date fechaInicio){
        this.fecha_inicio=fechaInicio;
    }

    public Date getFechaFin() {
        return fecha_fin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fecha_fin = fechaFin;
    }

    public Double getResultado() {
        return resultado;
    }
    public void setResultado(Double resultado) {
        this.resultado = resultado;
    }
}
