package com.keendo.wxpay.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 小程序拉起支付参数
 */
public class MiniAppPayParam {
    private String timeStamp;

    private String nonceStr;

    private String signType;

    private String paySign;

    @JsonProperty("package")
    private String packAge;

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
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

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPackAge() {
        return packAge;
    }

    public void setPackAge(String packAge) {
        this.packAge = packAge;
    }
}
