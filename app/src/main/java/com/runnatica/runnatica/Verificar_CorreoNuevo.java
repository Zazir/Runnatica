package com.runnatica.runnatica;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Verificar_CorreoNuevo extends AppCompatActivity {

    BottomNavigationView MenuUsuario;
    private String Correo;
    int Codigo, bandera = 0;
    Button Reenviar, Siguiente;
    EditText CodigoConfirmacion;
    TextView txtCorreo;

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText  msg;
    String rec, subject, textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar__correo_nuevo);

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        Reenviar = (Button)findViewById(R.id.btnReenviar);
        Siguiente = (Button)findViewById(R.id.btnVerificar);
        CodigoConfirmacion = (EditText)findViewById(R.id.etCodigoConfirmacion);
        txtCorreo = (TextView)findViewById(R.id.txtCorreo);
        getLastViewData();
        txtCorreo.setText(Correo);
        if(bandera == 0){
            bandera = 1;
            correo();
        }

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    home();
                }
                if (menuItem.getItemId() == R.id.menu_busqueda) {
                    Busqueda();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    Historial();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    Ajustes();
                }

                return true;
            }
        });
        Reenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correo();
            }
        });

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){
                    Siguiente();
                }else{
                    Toast.makeText(getApplicationContext(), "Error en el Codigo de Verificación" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getLastViewData() {
        Bundle extra = Verificar_CorreoNuevo.this.getIntent().getExtras();
        Correo = extra.getString("Correo");
    }
    private int CodigoRandom(){
        int numero = (int) (Math.random() * 9999) + 1;
        return numero;
    }
    public void correo(){
        Codigo = CodigoRandom();
        rec = Correo;
        subject = "Codigo de Verificación Runnatica";
        textMessage = "El codigo de verificación para activar su cuenta es: "+ Codigo;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("runnaticapp@gmail.com", "Lotoloto2125");
            }
        });

        pdialog = ProgressDialog.show(context, "", "Enviando Codigo...", true);

       RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }
    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Codigo Enviado a: " + Correo, Toast.LENGTH_LONG).show();
        }
    }
    private boolean Validaciones(){
        Boolean siguiente = false;
        if(CodigoConfirmacion.getText().toString().equals(String.valueOf(Codigo))){
            siguiente = true;
        }else{
            CodigoConfirmacion.setError("Error en el Codigo de Verificación");
        }
        return siguiente;
    }

    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
    private void Busqueda(){
        Intent next = new Intent(this, busqueda_competidor.class);
        startActivity(next);
    }
    private void Historial(){
        Intent next = new Intent(this, historial_competidor.class);
        startActivity(next);
    }
    private void Ajustes(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }
    private void Siguiente(){
        Toast.makeText(getApplicationContext(), "Codigo Correcto", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Verificar_CorreoNuevo.this, registro_usuario.class);
        intent.putExtra("Correo", Correo);
        startActivity(intent);
        finish();
    }
}