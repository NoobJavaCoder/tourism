package com.keendo.user.sdk.wx.util;

public class WxApiUrlUtil {

    private static final String CODE_2_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=#{app_id}&secret=#{secret}&js_code=#{js_code}&grant_type=authorization_code";


    /**
     * 拼接获取小程序登录校验信息api的url
     * @param appId:小程序appId
     * @param secret:小程序secret
     * @param code:登陆code
     * @return
     */
    public static String getMiniAppLoginVerifyUrl(String appId,String secret,String code){
        String url = CODE_2_SESSION_URL;
        url = url.replace("#{app_id}",appId);
        url = url.replace("#{secret}",secret);
        url = url.replace("#{js_code}",code);
        return url;
    }
}
