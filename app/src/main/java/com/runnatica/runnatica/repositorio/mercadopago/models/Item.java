
package com.runnatica.runnatica.repositorio.mercadopago.models;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("category_id")
    private String mCategoryId;
    @SerializedName("currency_id")
    private String mCurrencyId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("id")
    private String mId;
    @SerializedName("picture_url")
    private String mPictureUrl;
    @SerializedName("quantity")
    private Integer mQuantity;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("unit_price")
    private Double mUnitPrice;

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String categoryId) {
        mCategoryId = categoryId;
    }

    public String getCurrencyId() {
        return mCurrencyId;
    }

    public void setCurrencyId(String currencyId) {
        mCurrencyId = currencyId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Integer quantity) {
        mQuantity = quantity;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Double getUnitPrice() {
        return mUnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        mUnitPrice = unitPrice;
    }

}
