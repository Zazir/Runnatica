package com.runnatica.runnatica.poho;

public class Usuario {
    private int id;
    private int fechaNacimiento;
    private String nombre;
    private String tipoUsuario;
    private String correo;

    public Usuario(int id, int fechaNacimiento, String nombre, String tipoUsuario, String correo) {
        this.id = id;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
        this.correo = correo;
    }

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
