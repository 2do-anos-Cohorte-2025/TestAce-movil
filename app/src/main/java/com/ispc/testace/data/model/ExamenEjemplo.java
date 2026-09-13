package com.ispc.testace.data.model;

public class ExamenEjemplo {
    public static  Examen crearExamenHarcodeado(){
        Examen examen = new Examen(
                1,
                "Examen de Prueba",
                "Descripcion del examen",
                "Historia",
                "Intemedio",
                30,
                "https://1.bp.blogspot.com/-PJosauM0jns/U4aeazAUMqI/AAAAAAAAAC0/PAUR2KZiTv0/s1600/la+historia+del+mundo+en+2+horas.jpg"
        );
        Pregunta pregunta_1= new Pregunta(
                1,
                "¿Cuál es la capital de Francia?",
                "opcion_multiple",
                1.0,
                null
        );
        pregunta_1.agregarOpcion(new Opcion(1, "Madrid", false, null));
        pregunta_1.agregarOpcion(new Opcion(2, "París", true, null));
        pregunta_1.agregarOpcion(new Opcion(3, "Berlín", false, null));
        Pregunta pregunta_2=new Pregunta(
                2,
                "San Martin nacio en Uruguay",
                "verdadero_falso",
                1.0,
                null
        );
        pregunta_2.agregarOpcion(new Opcion(1,"Verdadero", false, null));
        pregunta_2.agregarOpcion(new Opcion(2, "Falso", true, null));

        examen.agregarPregunta(pregunta_1);
        examen.agregarPregunta(pregunta_2);

        return examen;
    }

}
