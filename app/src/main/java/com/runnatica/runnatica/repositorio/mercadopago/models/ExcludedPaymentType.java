
package com.runnatica.runnatica.repositorio.mercadopago.models;


import com.google.gson.annotations.SerializedName;


public class ExcludedPaymentType {

    @SerializedName("id")
    private String mId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}
