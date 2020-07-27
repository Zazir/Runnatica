package com.runnatica.runnatica.repositorio.mercadopago.models;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePago {

    @SerializedName("additional_info")
    private String mAdditionalInfo;
    @SerializedName("auto_return")
    private String mAutoReturn;
    @SerializedName("back_urls")
    private BackUrls mBackUrls;
    @SerializedName("client_id")
    private String mClientId;
    @SerializedName("collector_id")
    private Long mCollectorId;
    @SerializedName("date_created")
    private String mDateCreated;
    @SerializedName("expiration_date_from")
    private Object mExpirationDateFrom;
    @SerializedName("expiration_date_to")
    private Object mExpirationDateTo;
    @SerializedName("expires")
    private Boolean mExpires;
    @SerializedName("external_reference")
    private String mExternalReference;
    @SerializedName("id")
    private String mId;
    @SerializedName("init_point")
    private String mInitPoint;
    @SerializedName("items")
    private List<Item> mItems;
    @SerializedName("marketplace")
    private String mMarketplace;
    @SerializedName("marketplace_fee")
    private Long mMarketplaceFee;
    @SerializedName("notification_url")
    private Object mNotificationUrl;
    @SerializedName("operation_type")
    private String mOperationType;
    @SerializedName("payer")
    private Payer mPayer;
    @SerializedName("payment_methods")
    private PaymentMethods mPaymentMethods;
    @SerializedName("sandbox_init_point")
    private String mSandboxInitPoint;
    @SerializedName("shipments")
    private Shipments mShipments;

    public String getAdditionalInfo() {
        return mAdditionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        mAdditionalInfo = additionalInfo;
    }

    public String getAutoReturn() {
        return mAutoReturn;
    }

    public void setAutoReturn(String autoReturn) {
        mAutoReturn = autoReturn;
    }

    public BackUrls getBackUrls() {
        return mBackUrls;
    }

    public void setBackUrls(BackUrls backUrls) {
        mBackUrls = backUrls;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    public Long getCollectorId() {
        return mCollectorId;
    }

    public void setCollectorId(Long collectorId) {
        mCollectorId = collectorId;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public Object getExpirationDateFrom() {
        return mExpirationDateFrom;
    }

    public void setExpirationDateFrom(Object expirationDateFrom) {
        mExpirationDateFrom = expirationDateFrom;
    }

    public Object getExpirationDateTo() {
        return mExpirationDateTo;
    }

    public void setExpirationDateTo(Object expirationDateTo) {
        mExpirationDateTo = expirationDateTo;
    }

    public Boolean getExpires() {
        return mExpires;
    }

    public void setExpires(Boolean expires) {
        mExpires = expires;
    }

    public String getExternalReference() {
        return mExternalReference;
    }

    public void setExternalReference(String externalReference) {
        mExternalReference = externalReference;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInitPoint() {
        return mInitPoint;
    }

    public void setInitPoint(String initPoint) {
        mInitPoint = initPoint;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

    public String getMarketplace() {
        return mMarketplace;
    }

    public void setMarketplace(String marketplace) {
        mMarketplace = marketplace;
    }

    public Long getMarketplaceFee() {
        return mMarketplaceFee;
    }

    public void setMarketplaceFee(Long marketplaceFee) {
        mMarketplaceFee = marketplaceFee;
    }

    public Object getNotificationUrl() {
        return mNotificationUrl;
    }

    public void setNotificationUrl(Object notificationUrl) {
        mNotificationUrl = notificationUrl;
    }

    public String getOperationType() {
        return mOperationType;
    }

    public void setOperationType(String operationType) {
        mOperationType = operationType;
    }

    public Payer getPayer() {
        return mPayer;
    }

    public void setPayer(Payer payer) {
        mPayer = payer;
    }

    public PaymentMethods getPaymentMethods() {
        return mPaymentMethods;
    }

    public void setPaymentMethods(PaymentMethods paymentMethods) {
        mPaymentMethods = paymentMethods;
    }

    public String getSandboxInitPoint() {
        return mSandboxInitPoint;
    }

    public void setSandboxInitPoint(String sandboxInitPoint) {
        mSandboxInitPoint = sandboxInitPoint;
    }

    public Shipments getShipments() {
        return mShipments;
    }

    public void setShipments(Shipments shipments) {
        mShipments = shipments;
    }

}
