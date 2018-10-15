package com.keendo.biz.service;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.keendo.architecture.utils.Log;
import com.keendo.biz.service.CfgService.Constants;
import com.keendo.biz.service.bean.sms.KdSmsSingleSenderResult;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class COSSmsService {

    @Value("${sms.appid}")
    private String appid;

    @Value("${sms.appkey}")
    private String appkey;

    @Autowired
    private CfgService cfgService;

    private static final String TXSERVER_URL = "https://yun.tim.qq.com/v5/tlssmssvr/sendsms?sdkappid=%s&random=%s";

    private static final String NATIONCODE = "86";


    /**
     * 发送短信
     *
     * @param phoneNumber:手机号码
     * @param templateId:模板id
     * @param params:短信模板参数
     * @return 返回信息
     */
    public KdSmsSingleSenderResult sendMsg(String phoneNumber, Integer templateId, String... params) {

        String smsSign = cfgService.get(Constants.SMS_SIGN_KEY);

        KdSmsSingleSenderResult kdResult = null;

        try {

            SmsSingleSender ssender = new SmsSingleSender(Integer.parseInt(appid), appkey);

            SmsSingleSenderResult result = ssender.sendWithParam(NATIONCODE, phoneNumber,
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信

            kdResult = (KdSmsSingleSenderResult) result;

            System.out.println(result);

        } catch (HTTPException e) {
            // HTTP响应码错误
            Log.e(e);
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            Log.e(e);
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            Log.e(e);
            e.printStackTrace();
        }

        return kdResult;
    }


}
