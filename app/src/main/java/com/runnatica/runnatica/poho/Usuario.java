package com.runnatica.runnatica.poho;

public class Usuario {
    private static Usuario usuario;
    private int id;
    private int fechaNacimiento;
    private String nombre;
    private String tipoUsuario;
    private String correo;

    private Usuario() {
    }

    public Usuario(int id, int fechaNacimiento, String nombre, String tipoUsuario, String correo) {
        this.id = id;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
        this.correo = correo;
    }

    public static Usuario getUsuarioInstance() {
        if (usuario == null){
            usuario = new Usuario();
        }
        return usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEdadUsuario() {
        //1-4-1-1-1-9-9-9
        //0-1-2-3-4-5-6-7
        String fecha = ""+fechaNacimiento;
        String dia = fecha.substring(0, 2);
        String mes = fecha.substring(2, 4);
        String ano = fecha.substring(4);

        /*DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaNac = LocalDate.parse(dia+"/" + mes + "/" + ano, fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        String edad = "" + periodo.getYears() + periodo.getMonths() + periodo.getDays();*/
        return dia + mes + ano;
    }
}
