package com.runnatica.runnatica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

public class reCaptcha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_captcha);
    }
    Button btnVerify;
    TextView tvVerify;
    String TAG = reCaptcha.class.getSimpleName();
    String SITE_KEY = " SITE_KEY";
    String SITE_SECRET_KEY = " SITE_SECRET_KEY";
    RequestQueue queue;


}