package com.runnatica.runnatica.repositorio.mercadopago.models;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentMethods {

    @SerializedName("default_installments")
    private Object mDefaultInstallments;
    @SerializedName("default_payment_method_id")
    private Object mDefaultPaymentMethodId;
    @SerializedName("excluded_payment_methods")
    private List<ExcludedPaymentMethod> mExcludedPaymentMethods;
    @SerializedName("excluded_payment_types")
    private List<ExcludedPaymentType> mExcludedPaymentTypes;
    @SerializedName("installments")
    private Object mInstallments;

    public Object getDefaultInstallments() {
        return mDefaultInstallments;
    }

    public void setDefaultInstallments(Object defaultInstallments) {
        mDefaultInstallments = defaultInstallments;
    }

    public Object getDefaultPaymentMethodId() {
        return mDefaultPaymentMethodId;
    }

    public void setDefaultPaymentMethodId(Object defaultPaymentMethodId) {
        mDefaultPaymentMethodId = defaultPaymentMethodId;
    }

    public List<ExcludedPaymentMethod> getExcludedPaymentMethods() {
        return mExcludedPaymentMethods;
    }

    public void setExcludedPaymentMethods(List<ExcludedPaymentMethod> excludedPaymentMethods) {
        mExcludedPaymentMethods = excludedPaymentMethods;
    }

    public List<ExcludedPaymentType> getExcludedPaymentTypes() {
        return mExcludedPaymentTypes;
    }

    public void setExcludedPaymentTypes(List<ExcludedPaymentType> excludedPaymentTypes) {
        mExcludedPaymentTypes = excludedPaymentTypes;
    }

    public Object getInstallments() {
        return mInstallments;
    }

    public void setInstallments(Object installments) {
        mInstallments = installments;
    }

}
