package com.keendo.biz.service;

import com.keendo.wxpay.service.IMiniAppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keendo.biz.service.CfgService.Constants;
/*用于支付模块*/
@Service
public class MiniAppPayConfigService implements IMiniAppConfigService {

    @Autowired
    private CfgService cfgService;

    @Override
    public String getAppId() {
        String appid = cfgService.get(Constants.MINIAPP_APP_ID_KEY);
        return appid;
    }

    @Override
    public String getSecret() {
        String secret = cfgService.get(Constants.MINIAPP_SECRET_KEY);
        return secret;
    }

    @Override
    public String getMchId() {
        String mchId = cfgService.get(Constants.MCH_ID_KEY);
        return mchId;
    }

    @Override
    public String getMchKey() {
        String mchKey = cfgService.get(Constants.MCH_KEY_KEY);
        return mchKey;
    }

    @Override
    public String getPayNotifyUrl() {
        String payNotifyUrl = cfgService.get(Constants.MINIAPP_PAY_NOTIFY_URL_KEY);
        return payNotifyUrl;
    }
}
