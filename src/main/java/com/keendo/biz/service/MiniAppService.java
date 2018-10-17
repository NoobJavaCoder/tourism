package com.keendo.biz.service;

import com.keendo.user.service.IMiniAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keendo.biz.service.CfgService.Constants;

@Service
public class MiniAppService implements IMiniAppService {

    @Autowired
    private CfgService cfgService;

    /**
     * 获取小程序appid
     * @return
     */
    @Override
    public String getAppId() {
        String appid = cfgService.get(Constants.MINIAPP_APP_ID_KEY);
        return appid;
    }

    /**
     * 获取小程序secret
     * @return
     */
    @Override
    public String getSecret() {
        String secret = cfgService.get(Constants.MINIAPP_SECRET_KEY);
        return secret;
    }
}
