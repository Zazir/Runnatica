package com.runnatica.runnatica.repositorio.mercadopago.models;

import com.google.gson.annotations.SerializedName;

public class Payer {

    @SerializedName("email")
    private String mEmail;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

}
