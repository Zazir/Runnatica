package com.runnatica.runnatica.poho;

public class Competencias {
    private int id;
    private String NombreCompetencia;
    private String DescripcionCompetencia;
    private int PrecioCompetencia;
    private String ImageCompetencia;

    public Competencias(int id, String nombreCompetencia, String descripcionCompetencia, int precioCompetencia, String imageCompetencia) {
        this.id = id;
        this.NombreCompetencia = nombreCompetencia;
        this.DescripcionCompetencia = descripcionCompetencia;
        this.PrecioCompetencia = precioCompetencia;
        this.ImageCompetencia = imageCompetencia;
    }

    public int getId() {
        return id;
    }

    public String getNombreCompetencia() {
        return NombreCompetencia;
    }

    public String getDescripcionCompetencia() {
        return DescripcionCompetencia;
    }

    public int getPrecioCompetencia() {
        return PrecioCompetencia;
    }

    public String getImageCompetencia() {
        return ImageCompetencia;
    }
}
