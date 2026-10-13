package com.ispc.testace.data.model;

        import com.google.gson.annotations.SerializedName;

public class ExamenCatalogo {
    private int id;
    private String titulo;
    private String slug;
    private String descripcion;

    @SerializedName("categoria_nombre")
    private String categoriaNombre;

    @SerializedName("nivel_nombre")
    private String nivelNombre;

    @SerializedName("tiempo_limite")
    private Integer tiempoLimite;

    @SerializedName("imagen_examen")
    private String imagenExamen;

    @SerializedName("usuario_nombre")
    private String usuarioNombre;

    @SerializedName("es_profesor")
    private boolean esProfesor;

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getSlug() { return slug; }
    public String getDescripcion() { return descripcion; }
    public String getCategoriaNombre() { return categoriaNombre; }
    public String getNivelNombre() { return nivelNombre; }
    public Integer getTiempoLimite() { return tiempoLimite; }
    public String getImagenExamen() { return imagenExamen; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public boolean isEsProfesor() { return esProfesor; }
}