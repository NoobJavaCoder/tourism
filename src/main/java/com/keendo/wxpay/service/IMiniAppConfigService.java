package com.keendo.wxpay.service;
public interface IMiniAppConfigService {
    /*获取小程序appId*/
    String getAppId();

    /*获取小程序secret*/
    String getSecret();

    /*获取微信商户id*/
    String getMchId();

    /*获取支付秘钥*/
    String getMchKey();

    /*获取支付回调url*/
    String getPayNotifyUrl();
}

