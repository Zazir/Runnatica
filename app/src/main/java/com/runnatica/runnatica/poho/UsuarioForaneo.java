package com.runnatica.runnatica.poho;

public class UsuarioForaneo {
    private int id_foreign;
    private String nombre;
    private String correon;
    private String sexo;
    private int edad;

    public UsuarioForaneo(int id_foreign, String nombre, String correon, String sexo, int edad) {
        this.id_foreign = id_foreign;
        this.nombre = nombre;
        this.correon = correon;
        this.sexo = sexo;
        this.edad = edad;
    }

    public int getId_foreign() {
        return id_foreign;
    }

    public void setId_foreign(int id_foreign) {
        this.id_foreign = id_foreign;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreon() {
        return correon;
    }

    public void setCorreon(String correon) {
        this.correon = correon;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
