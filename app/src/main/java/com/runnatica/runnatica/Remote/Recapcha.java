package com.runnatica.runnatica.Remote;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.runnatica.runnatica.Model.MyResponse;
import com.runnatica.runnatica.R;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Recapcha extends AppCompatActivity {

    Button btn_post;

    IreCATCHA mService;

        private static final String SITE_KEY_CAPTCHA ="6LcjYfoUAAAAAERBXq7YgNgz0TThmuoDc7wnuikU";

        private IreCATCHA getAPI()
        {
            return RetrofitClient.getClienr("https://runnatica.000webhostapp.com/WebServiceRunnatica/").create(IreCATCHA.class);
        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapcha);

        //Init Service
        mService = getAPI();

        //View
        btn_post = (Button)findViewById(R.id.btn_post);

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyNet.getClient(Recapcha.this)
                        .verifyWithRecaptcha(SITE_KEY_CAPTCHA)
                        .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse recaptchaTokenResponse) {
                                if(!recaptchaTokenResponse.getTokenResult().isEmpty())
                                    verifyTokenOnServer(recaptchaTokenResponse.getTokenResult());
                                
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(e instanceof ApiException) {
                                    ApiException apiException = (ApiException) e;
                                    Log.d("ERROREDMT", "Error : " + CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                                }else{
                                    Log.d("ERROREDMT", "Unk error");
                                }
                            }
                        });
            }
        });
    }

    private void verifyTokenOnServer(String tokenResult) {

            //show dialog
        final SpotsDialog dialog = new SpotsDialog(Recapcha.this);
        dialog.show();
        dialog.setMessage("Porfavor espere...");


            mService.validate(tokenResult)
                    .enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            dialog.dismiss();

                            if(response.body().isSuccess())
                                Toast.makeText(Recapcha.this, "Comment posted!", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(Recapcha.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            dialog.dismiss();

                            Log.d("ERRORREDMT", t.getMessage());
                        }
                    });
    }
}
