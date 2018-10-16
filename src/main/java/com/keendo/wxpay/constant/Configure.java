package com.keendo.wxpay.constant;

public class Configure {

    private static String KEY = "KEENdo";

    //小程序ID
    private static String APPID = "你的小程序id";
    //商户号
    private static String MCH_ID = "你的商户号";
    //secret
    private static String SECRET = "你的小程序的secret";

    private static String CERT_PATH = "";

    private static String NOTIFY_URL = "";

    private static String ORDER_QUERY_URL = "";


    public static String getOrderQueryUrl(){
        return ORDER_QUERY_URL;
    }

    public static String getNotifyUrl(){
        return NOTIFY_URL;
    }

    public static String getCertPath(){
        return CERT_PATH;
    }

    public static String getSecret() {
        return SECRET;
    }

    public static void setSecret(String secret) {
        Configure.SECRET = secret;
    }

    public static String getKey() {
        return KEY;
    }

    public static void setKey(String key) {
        Configure.KEY = key;
    }

    public static String getAppID() {
        return APPID;
    }

    public static void setAppID(String appID) {
        Configure.APPID = appID;
    }

    public static String getMchId() {
        return MCH_ID;
    }

    public static void setMchId(String mch_id) {
        Configure.MCH_ID = mch_id;
    }
}
