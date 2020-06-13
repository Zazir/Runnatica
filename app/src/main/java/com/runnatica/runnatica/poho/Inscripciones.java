package com.runnatica.runnatica.poho;

public class Inscripciones {
    private int id_competencia;
    private String nombreInscripcion;
    private int cantidadUsuarios;
    private int cantidadForaneos;
    private int edadMaxima;
    private int edadMinina;

    public Inscripciones(int id_competencia, String nombreInscripcion, int cantidadUsuarios, int cantidadForaneos, int edadMaxima, int edadMinina) {
        this.id_competencia = id_competencia;
        this.nombreInscripcion = nombreInscripcion;
        this.cantidadUsuarios = cantidadUsuarios;
        this.cantidadForaneos = cantidadForaneos;
        this.edadMaxima = edadMaxima;
        this.edadMinina = edadMinina;
    }

    public int getId_competencia() {
        return id_competencia;
    }

    public String getNombreInscripcion() {
        return nombreInscripcion;
    }

    public int getCantidadUsuarios() {
        return cantidadUsuarios;
    }

    public int getCantidadForaneos() {
        return cantidadForaneos;
    }

    public int getEdadMaxima() {
        return edadMaxima;
    }

    public int getEdadMinina() {
        return edadMinina;
    }
}
