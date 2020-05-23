package com.runnatica.runnatica.Remote;

import com.runnatica.runnatica.Model.MyResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import retrofit2.http.POST;

public interface IreCATCHA {
    @FormUrlEncoded
    @POST("google_recaptcha.php")
    Call<MyResponse> validate(@Field("recaptcha-response") String response);
}



