package com.runnatica.runnatica.poho;

public class UsuariosInscritos {
    private String nombre;
    private String correo;
    private String fecha_nacimiento;
    private String fecha_inscripcion;

    public UsuariosInscritos(String nombre, String correo, String fecha_nacimiento, String fecha_inscripcion) {
        this.nombre = nombre;
        this.correo = correo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_inscripcion = fecha_inscripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public String getFecha_inscripcion() {
        return fecha_inscripcion;
    }
}
