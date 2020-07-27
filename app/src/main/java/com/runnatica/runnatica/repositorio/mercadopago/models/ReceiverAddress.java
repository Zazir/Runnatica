package com.runnatica.runnatica.repositorio.mercadopago.models;
import com.google.gson.annotations.SerializedName;

public class ReceiverAddress {

    @SerializedName("apartment")
    private String mApartment;
    @SerializedName("floor")
    private String mFloor;
    @SerializedName("street_name")
    private String mStreetName;
    @SerializedName("street_number")
    private Object mStreetNumber;
    @SerializedName("zip_code")
    private String mZipCode;

    public String getApartment() {
        return mApartment;
    }

    public void setApartment(String apartment) {
        mApartment = apartment;
    }

    public String getFloor() {
        return mFloor;
    }

    public void setFloor(String floor) {
        mFloor = floor;
    }

    public String getStreetName() {
        return mStreetName;
    }

    public void setStreetName(String streetName) {
        mStreetName = streetName;
    }

    public Object getStreetNumber() {
        return mStreetNumber;
    }

    public void setStreetNumber(Object streetNumber) {
        mStreetNumber = streetNumber;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

}
