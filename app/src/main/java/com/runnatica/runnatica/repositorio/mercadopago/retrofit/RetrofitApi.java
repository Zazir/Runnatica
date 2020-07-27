package com.runnatica.runnatica.repositorio.mercadopago.retrofit;


import com.runnatica.runnatica.repositorio.mercadopago.models.PagoDetalles;
import com.runnatica.runnatica.repositorio.mercadopago.models.ResponsePago;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitApi {
    String TOKEN = "TEST-1745142307811262-072718-f1471019cd2a2125d481c0c5ee4aa5ff-613470576"; //reemplezar con su accestoken

    @POST ("preferences?access_token=" + TOKEN)
    Observable<ResponsePago> obtenerDatosPago(@Body PagoDetalles pagoDetalles);



}
