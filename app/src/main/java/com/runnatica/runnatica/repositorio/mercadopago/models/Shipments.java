package com.runnatica.runnatica.repositorio.mercadopago.models;
import com.google.gson.annotations.SerializedName;

public class Shipments {

    @SerializedName("receiver_address")
    private ReceiverAddress mReceiverAddress;

    public ReceiverAddress getReceiverAddress() {
        return mReceiverAddress;
    }

    public void setReceiverAddress(ReceiverAddress receiverAddress) {
        mReceiverAddress = receiverAddress;
    }

}
