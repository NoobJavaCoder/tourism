package com.keendo.biz.service;

import com.keendo.user.service.IMiniAppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*用于用户模块*/
@Service
public class MiniAppUserConfigService implements IMiniAppConfigService {
    @Autowired
    private CfgService cfgService;

    @Override
    public String getAppId() {
        String appid = cfgService.get(CfgService.Constants.MINIAPP_APP_ID_KEY);
        return appid;
    }

    @Override
    public String getSecret() {
        String secret = cfgService.get(CfgService.Constants.MINIAPP_SECRET_KEY);
        return secret;
    }
}
