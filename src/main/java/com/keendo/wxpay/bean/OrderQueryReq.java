package com.keendo.wxpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 订单查询请求参数
 */
public class OrderQueryReq {

    @XStreamAlias("appid")
    private String appId;

    @XStreamAlias("mch_id")
    private String mchId;

    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    @XStreamAlias("nonce_str")
    private String nonceStr;

    @XStreamAlias("sign")
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
