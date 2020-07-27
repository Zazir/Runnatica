package com.runnatica.runnatica.repositorio.mercadopago.models;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PagoDetalles {

    @SerializedName("items")
    private List<Item> mItems;
    @SerializedName("payer")
    private Payer mPayer;
    @SerializedName("payment_methods")
    private PaymentMethods mPayment_methods;

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

    public Payer getPayer() {
        return mPayer;
    }

    public void setPayer(Payer payer) {
        mPayer = payer;
    }

    public PaymentMethods getmPayment_methods() {
        return mPayment_methods;
    }

    public void setmPayment_methods(PaymentMethods mPayment_methods) {
        this.mPayment_methods = mPayment_methods;
    }
}
