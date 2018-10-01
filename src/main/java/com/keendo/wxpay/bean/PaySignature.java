package com.keendo.wxpay.bean;

import com.keendo.wxpay.utils.TimeUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 小程序调起支付数据签名字段
 */
public class PaySignature {

    private String appId;

    private String timeStamp;

    private String nonceStr;

    @XStreamAlias("package")
    private String packAge;

    private String signType;

    public PaySignature(){}

    public PaySignature(String appId, String nonceStr, String prepayId) {
        this.appId = appId;
        this.nonceStr = nonceStr;
        this.signType = "MD5";
        this.timeStamp = TimeUtil.getCurrentTimestamp().toString();
        this.packAge = "prepay_id=" + prepayId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackAge() {
        return packAge;
    }

    public void setPackAge(String packAge) {
        this.packAge = packAge;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }
}
