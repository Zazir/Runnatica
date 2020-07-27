
package com.runnatica.runnatica.repositorio.mercadopago.models;

import com.google.gson.annotations.SerializedName;

public class BackUrls {

    @SerializedName("failure")
    private String mFailure;
    @SerializedName("pending")
    private String mPending;
    @SerializedName("success")
    private String mSuccess;

    public String getFailure() {
        return mFailure;
    }

    public void setFailure(String failure) {
        mFailure = failure;
    }

    public String getPending() {
        return mPending;
    }

    public void setPending(String pending) {
        mPending = pending;
    }

    public String getSuccess() {
        return mSuccess;
    }

    public void setSuccess(String success) {
        mSuccess = success;
    }

}
