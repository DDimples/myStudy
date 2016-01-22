package com.mystudy.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 程祥 on 16/1/12.
 * Function：
 */
public class PaymentDTO implements Serializable{


    private String orderId;
    private String tradeToken;
    private double orderAmount;
    private String notifyUrl;
    private int bizType;
    private String successUrl;
    private String errorUrl;
    private String cancelUrl;
    private long cardNo;
    private String title;
    private List<OrderDetailEntity> orderDetail;
    private List<OrderDetailEntity> tips;
    private String thirdPartyPaymentTitle;
    private String thirdPartyPaymentBody;
    private String orderAmountTitle;
    private int language;
    private int currencyType;
    private String accessToken;
    private String channelId;
    private String sessionToken;
    private String loginBackUrl;
    private String noLoginBackUrl;
    private List<String> payTypes;
    private boolean showHeaderFlag;
    private String indexBackBtnText;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeToken() {
        return tradeToken;
    }

    public void setTradeToken(String tradeToken) {
        this.tradeToken = tradeToken;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public long getCardNo() {
        return cardNo;
    }

    public void setCardNo(long cardNo) {
        this.cardNo = cardNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OrderDetailEntity> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetailEntity> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public List<OrderDetailEntity> getTips() {
        return tips;
    }

    public void setTips(List<OrderDetailEntity> tips) {
        this.tips = tips;
    }

    public String getThirdPartyPaymentTitle() {
        return thirdPartyPaymentTitle;
    }

    public void setThirdPartyPaymentTitle(String thirdPartyPaymentTitle) {
        this.thirdPartyPaymentTitle = thirdPartyPaymentTitle;
    }

    public String getThirdPartyPaymentBody() {
        return thirdPartyPaymentBody;
    }

    public void setThirdPartyPaymentBody(String thirdPartyPaymentBody) {
        this.thirdPartyPaymentBody = thirdPartyPaymentBody;
    }

    public String getOrderAmountTitle() {
        return orderAmountTitle;
    }

    public void setOrderAmountTitle(String orderAmountTitle) {
        this.orderAmountTitle = orderAmountTitle;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getLoginBackUrl() {
        return loginBackUrl;
    }

    public void setLoginBackUrl(String loginBackUrl) {
        this.loginBackUrl = loginBackUrl;
    }

    public String getNoLoginBackUrl() {
        return noLoginBackUrl;
    }

    public void setNoLoginBackUrl(String noLoginBackUrl) {
        this.noLoginBackUrl = noLoginBackUrl;
    }

    public List<String> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<String> payTypes) {
        this.payTypes = payTypes;
    }

    public boolean isShowHeaderFlag() {
        return showHeaderFlag;
    }

    public void setShowHeaderFlag(boolean showHeaderFlag) {
        this.showHeaderFlag = showHeaderFlag;
    }

    public String getIndexBackBtnText() {
        return indexBackBtnText;
    }

    public void setIndexBackBtnText(String indexBackBtnText) {
        this.indexBackBtnText = indexBackBtnText;
    }
}
