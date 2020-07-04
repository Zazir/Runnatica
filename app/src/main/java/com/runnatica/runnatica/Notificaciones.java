package com.runnatica.runnatica;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

public class Notificaciones extends AppCompatActivity {
    private Switch swNotifications;
    private PendingIntent pendingIntent;
    private final static String CHANEL_ID = "NOTIFICATION";
    private final static int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        swNotifications = (Switch)findViewById(R.id.swNotificaciones);


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
}
