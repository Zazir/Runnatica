package com.runnatica.runnatica.poho;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public int getEdadUsuario() {
        //1-4-1-1-1-9-9-9
        //0-1-2-3-4-5-6-7
        String fecha = ""+fechaNacimiento;
        String dia = fecha.substring(0, 2);
        String mes = fecha.substring(2, 4);
        String ano = fecha.substring(4);
        String dateBorn = dia+"-"+mes+"-"+ano;

        Date startdate = null;
        Date enddate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        enddate.getDate();

        try {
            startdate = formatter.parse(dateBorn);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startdate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(enddate);

        int monthCount = 0;
        int firstDayInFirstMonth = startCalendar.get(Calendar.DAY_OF_MONTH);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        endCalendar.add(Calendar.DAY_OF_YEAR, -firstDayInFirstMonth + 1);

        while (!startCalendar.after(endCalendar)) {
            startCalendar.add(Calendar.MONTH, 1);
            ++monthCount;
        }

        startCalendar.add(Calendar.MONTH, -1);
        --monthCount;
        int remainingDays = 0;
        while (!startCalendar.after(endCalendar)) {
            startCalendar.add(Calendar.DAY_OF_YEAR, 1);
            ++remainingDays;
        }

        startCalendar.add(Calendar.DAY_OF_YEAR, -1);
        --remainingDays;

        int lastMonthMaxDays = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (remainingDays >= lastMonthMaxDays) {
            ++monthCount;
            remainingDays -= lastMonthMaxDays;
        }

        int diffMonth = monthCount % 12;
        int diffYear = monthCount / 12;
        int diffDay = remainingDays;

        return diffYear;
    }
}
