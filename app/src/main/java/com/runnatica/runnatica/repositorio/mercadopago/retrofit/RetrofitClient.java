package com.runnatica.runnatica.repositorio.mercadopago.retrofit;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit newInstance;

    public static Retrofit getInstance(){
        if(newInstance == null){


            newInstance = new Retrofit.Builder()
                    .baseUrl("https://api.mercadopago.com/checkout/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return  newInstance;
    }

}
