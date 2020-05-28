package com.runnatica.runnatica;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

public class ReCaptcha extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    CheckBox checkBox;
    GoogleApiClient googleApiClient;

    //poner las SiteKey en el String
    String Sitekey = "6LdNH_0UAAAAAHi4jOHkOPxrVRKO5b_xnt1-ZT-Z";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_captcha);

        checkBox = (CheckBox)findViewById(R.id.check_box);

        //Crear el Google Api Clint

        googleApiClient = new GoogleApiClient.Builder(this)
            .addApi(SafetyNet.API)
            .addConnectionCallbacks(ReCaptcha.this)
            .build();
        googleApiClient.connect();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){

                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,Sitekey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status =recaptchaTokenResult.getStatus();
                                    if((status !=null && status.isSuccess())){
                                        //Mensaje de que se acepto la peticion y no es un robot
                                        Toast.makeText(getApplicationContext(), "Felicidades, no eres un robot", Toast.LENGTH_SHORT).show();
                                        //cambiar el checkBox de color
                                        checkBox.setTextColor(Color.GREEN);

                                    }
                                }
                            });
                }else{
                    checkBox.setTextColor(Color.BLACK);
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
