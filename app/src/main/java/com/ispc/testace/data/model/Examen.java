package com.ispc.testace.data.model;

import java.util.ArrayList;
import java.util.List;

public class Examen {
    private int id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String nivel;
    private int tiempoLimite;
    private String imagenExamen;
    private List<Pregunta> preguntas;

    public Examen(int id, String titulo, String descripcion, String categoria, String nivel, int tiempoLimite, String imagenExamen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nivel = nivel;
        this.tiempoLimite = tiempoLimite;
        this.imagenExamen = imagenExamen;
        this.preguntas = new ArrayList<>();
    }

    public void agregarPregunta(Pregunta pregunta) {
        preguntas.add(pregunta);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo(){
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo=titulo;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    public String getCategoria(){
        return categoria;
    }
    public void setCategoria(String categoria){
        this.categoria=categoria;
    }
    public String getNivel(){
        return nivel;
    }
    public void setNivel(String nivel){
        this.nivel=nivel;
    }
    public int getTiempoLimite(){
        return  tiempoLimite;
    }
    public void setTiempoLimite(int tiempoLimite){
        this.tiempoLimite=tiempoLimite;
    }
    public String getImagenExamen(){
        return imagenExamen;
    }
    public void setImagenExamen(String imagenExamen){
        this.imagenExamen=imagenExamen;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
