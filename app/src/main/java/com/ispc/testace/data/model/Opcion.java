package com.ispc.testace.data.model;

import com.google.gson.annotations.SerializedName;

public class Opcion {
    private int id;

    // El serializador no podia adaptar los datos correctamente a la class, para ello el @SerializedName
    @SerializedName("texto_opcion")
    private String textoOpcion;

    @SerializedName("es_correcta")
    private boolean esCorrecta;

    @SerializedName("imagen_opcion")
    private String imagenOpcion;

    private boolean isSelected;
    public Opcion() {}

        // Getters y setters
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

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public String getImagenOpcion() {
        return imagenOpcion;
    }
    public void setImagenOpcion(String imagenOpcion) {
        this.imagenOpcion = imagenOpcion;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
