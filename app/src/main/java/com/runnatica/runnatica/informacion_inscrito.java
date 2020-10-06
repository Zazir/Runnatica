package com.runnatica.runnatica;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class informacion_inscrito extends AppCompatActivity {
    private TextView txtNombre, txtCorreo, txtF_inscrito, txtF_nacimiento;
    BottomNavigationView MenuOrganizador;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_inscrito);
        txtNombre = (TextView)findViewById(R.id.tvNombreinscrito);
        txtCorreo = (TextView)findViewById(R.id.tvCorreoInscrito);
        txtF_inscrito = (TextView)findViewById(R.id.tvFechaInscripcion2);
        txtF_nacimiento = (TextView)findViewById(R.id.tvNacimientoUsuario2);

        MenuOrganizador= (BottomNavigationView)findViewById(R.id.MenuOrganizador);

        Menu menu = MenuOrganizador.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);

        MenuOrganizador.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    homeOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    historialOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    ajuestesOrganizador();
                }

                return true;
            }
        });
        getLastViewData();
    }

    private void getLastViewData() {
        Bundle extra = informacion_inscrito.this.getIntent().getExtras();
        txtNombre.setText(extra.getString("NOMBRE"));
        txtCorreo.setText(extra.getString("CORREO"));
        txtF_inscrito.setText(PonerMes(extra.getString("F_INSCRIPCION"))+ "");
        txtF_nacimiento.setText(CalcularEdad(extra.getString("F_NACIMIENTO"))+ " Años");
        //id_competencia = extra.getString("id");
    }

    private void homeOrganizador(){
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
    }
    private void historialOrganizador(){
        Intent next = new Intent(this, historial_organizador.class);
        startActivity(next);
    }
    private void ajuestesOrganizador(){
        Intent next = new Intent(this, ajustes_organizador.class);
        startActivity(next);
    }
    public int CalcularEdad(String fechaNacimiento) {
        String fecha = fechaNacimiento;
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
    private String PonerMes(String diaCompetencia){
        String Temp[] = diaCompetencia.split("-");
        String Temp2[] = Temp[2].split(" ");
        int Mes = Integer.parseInt(Temp[1]);
        Log.i("Mes", "Mes numero: " + Mes);

        String MES = "";

        switch(Mes){
            case 1:
                MES = "Enero";
                break;
            case 2:
                MES = "Febrero";
                break;
            case 3:
                MES = "Marzo";
                break;
            case 4:
                MES = "Abril";
                break;
            case 5:
                MES = "Mayo";
                break;
            case 6:
                MES = "Junio";
                break;
            case 7:
                MES = "Julio";
                break;
            case 8:
                MES = "Agosto";
                break;
            case 9:
                MES = "Septiembre";
                break;
            case 10:
                MES = "Octubre";
                break;
            case 11:
                MES = "Noviembre";
                break;
            case 12:
                MES = "Diciembre";
                break;
        }

        return Temp2[0] + " de " + MES + " del " + Temp[0];
    }
}