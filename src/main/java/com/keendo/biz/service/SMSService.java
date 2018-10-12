package com.keendo.biz.service;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.keendo.biz.service.bean.sms.KdSmsSingleSenderResult;
import com.keendo.biz.service.constant.GlobalConfigConstants;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class SMSService {

    @Value("${sms.appid}")
    private String appid;

    @Value("${sms.appkey}")
    private String appkey;

    @Autowired
    private CfgService cfgService;

    private static final String TXSERVER_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=%s&random=%s";

    private static final String NATIONCODE = "86";


    /**
     * 发送短信验证码
     *
     * @param phoneNumber:手机号码
     * @param verifyCode:验证码
     * @return 返回信息
     */
    public KdSmsSingleSenderResult sendCode(String phoneNumber, String verifyCode) {

        String smsSign = cfgService.get(GlobalConfigConstants.SMS_SIGN_KEY);

        Integer templateId = cfgService.getInteger(GlobalConfigConstants.SMS_TPL_ID_KEY);

        ArrayList<String> params = new ArrayList<>();
        params.add(verifyCode);

        KdSmsSingleSenderResult kdResult = null;

        try {

            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(appid), appkey);

            SmsSingleSenderResult result = ssender.sendWithParam(NATIONCODE, phoneNumber,
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信

            kdResult = (KdSmsSingleSenderResult) result;

            System.out.println(result);

        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

        return kdResult;
    }


}
