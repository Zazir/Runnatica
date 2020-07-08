package com.runnatica.runnatica;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

public class Notificaciones extends AppCompatActivity {
    private Switch swNotifications;
    private PendingIntent pendingIntent;
    private final static String CHANEL_ID = "NOTIFICATION";
    private final static int NOTIFICATION_ID = 0;

    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        swNotifications = (Switch)findViewById(R.id.swNotificaciones);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

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


    }

    public void onclick(View view) {
        if (view.getId() == R.id.swNotificaciones) {
            if (swNotifications.isChecked()) {
                sendPendingIntent();
                crearNotificacionOreo();
                crearNotificacion();
            }
        }
    }

    private void sendPendingIntent() {
        Intent intent = new Intent(Notificaciones.this, home.class);
    }

    private void crearNotificacionOreo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification";
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void crearNotificacion() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANEL_ID);

        notificationBuilder.setSmallIcon(R.drawable.reloj);
        notificationBuilder.setContentTitle("Notificacion android");
        notificationBuilder.setContentText("Pinche madre pues");
        notificationBuilder.setColor(Color.GREEN);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setLights(Color.MAGENTA, 1000, 1000);
        notificationBuilder.setVibrate(new long[]{1000, 1000});

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());
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
}
