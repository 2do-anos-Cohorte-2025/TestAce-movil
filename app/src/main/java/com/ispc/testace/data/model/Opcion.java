package com.ispc.testace.data.model;

public class Opcion {
    private int id;
    private String textoOpcion;
    private boolean esCorrecta;
    private String imagenOpcion;

    public Opcion(int id, String textoOpcion, boolean esCorrecta, String imagenOpcion) {
        this.id = id;
        this.textoOpcion = textoOpcion;
        this.esCorrecta = esCorrecta;
        this.imagenOpcion = imagenOpcion;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTextoOpcion() {
        return textoOpcion;
    }
    public void setTextoOpcion(String textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

    public boolean esCorrecta() {
        return esCorrecta;
    }

    public String getImagenOpcion() {
        return imagenOpcion;
    }

    public void setImagenOpcion(String imagenOpcion) {
        this.imagenOpcion = imagenOpcion;
    }

}
