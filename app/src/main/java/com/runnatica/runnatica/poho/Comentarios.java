package com.runnatica.runnatica.poho;

public class Comentarios {
    private String id_foro;
    private String nombre_usuario;
    private int id_competencia;
    private String mensaje;
    private String tipo_comentario;

    public Comentarios(String id_foro, String mensaje, String nombre_usuario, String tipo_comentario) {
        this.id_foro = id_foro;
        this.nombre_usuario = nombre_usuario;
        //this.id_competencia = id_competencia;
        this.mensaje = mensaje;
        this.tipo_comentario = tipo_comentario;
    }

    public String getId_foro() {
        return id_foro;
    }

    public String getnombre_usuario() {
        return nombre_usuario;
    }

    public int getId_competencia() {
        return id_competencia;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getTipo_comentario() {
        return tipo_comentario;
    }
}
