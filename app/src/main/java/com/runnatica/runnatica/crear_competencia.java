package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class crear_competencia extends AppCompatActivity {

    private EditText Nombre, Precio, Dia, Mes, Ano, Hora, GradosUbicacion, Ciudad, Colonia, Calle, Descripcion;
    private Button Informacion, Imagen, Aval, Guardar;
    private Spinner Pais, Estado;
    private RadioButton SiReembolso, NoReembolso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia);

        Nombre=(EditText)findViewById(R.id.etNombreCompetencia);
        Precio = (EditText)findViewById(R.id.etPrecioCompetencia);
        Informacion = (Button)findViewById(R.id.btnInformacionPrecio);
        Dia = (EditText)findViewById(R.id.etDiaCompetencia);
        Mes = (EditText)findViewById(R.id.etMesCompetencia);
        Ano = (EditText)findViewById(R.id.etAno);
        Hora = (EditText)findViewById(R.id.etHoraCompetencia);
        Imagen = (Button) findViewById(R.id.btnImagenCompetencia);
        GradosUbicacion = (EditText)findViewById(R.id.etGradosUbicacion);
        Ciudad = (EditText)findViewById(R.id.etCiudadCompetencia);
        Colonia = (EditText)findViewById(R.id.etColoniaCompetencia);
        Pais = (Spinner) findViewById(R.id.spPaisCompetencia);
        Estado = (Spinner) findViewById(R.id.spEstadoCompetencia);
        Calle = (EditText)findViewById(R.id.etCalleCompetencia);
        Aval = (Button) findViewById(R.id.btnAvalCompetencia);
        Descripcion = (EditText)findViewById(R.id.etDescripcionCompetencia);
        SiReembolso = (RadioButton) findViewById(R.id.rbSiReembolso);
        NoReembolso = (RadioButton) findViewById(R.id.rbNoReembolso);
        Guardar = (Button) findViewById(R.id.btnGuardarCompetencia);
    }


}
