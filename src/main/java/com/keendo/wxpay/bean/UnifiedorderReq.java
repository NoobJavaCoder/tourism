package com.keendo.wxpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 统一下单请求参数
 */
public class UnifiedorderReq {
    /*小程序appId*/
    @XStreamAlias("appid")
    private String appId;

    /*商户id*/
    @XStreamAlias("mch_id")
    private String mchId;

    /*随机字符串*/
    @XStreamAlias("nonce_str")
    private String nonceStr;

    /*签名*/
    @XStreamAlias("sign")
    private String sign;

    /*支付商品描述*/
    @XStreamAlias("body")
    private String body;

    /*商品详情,优惠场景*/
    @XStreamAlias("detail")
    private String detail;

    /*系统生成的订单号*/
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /*ip*/
    @XStreamAlias("spbill_create_ip")
    private String spBillCreateIp;

    /*商品优惠标记,优惠场景*/
    @XStreamAlias("goods_tag")
    private String goodsTag;

    /*支付金额*/
    @XStreamAlias("total_fee")
    private String totalFee;

    /*回调地址*/
    @XStreamAlias("notify_url")
    private String notifyUrl;

    /*交易类型,小程序类型*/
    @XStreamAlias("trade_type")
    private String tradeType;

    /*付款人openid*/
    @XStreamAlias("openid")
    private String openId;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSpBillCreateIp() {
        return spBillCreateIp;
    }

    public void setSpBillCreateIp(String spBillCreateIp) {
        this.spBillCreateIp = spBillCreateIp;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
