package com.keendo.user.service.helper;

import com.keendo.architecture.exception.BizException;
import com.keendo.architecture.utils.Log;
import com.keendo.user.sdk.wx.util.HttpsHelper;
import com.keendo.user.sdk.wx.util.WxApiUrlUtil;
import com.keendo.user.service.IMiniAppService;
import com.keendo.user.service.helper.bean.LoginVerifyInfo;
import com.keendo.user.service.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiniAppHelper {

    @Autowired
    private IMiniAppService miniAppService;

    /**
     * 小程序用户登录,通过code获取openId,unionId,session_key
     *
     * @param jsCode
     * @return
     */
    private String getfetchLoginVerifyUrl(String appId, String appSecret, String jsCode) {

        String url = WxApiUrlUtil.getMiniAppLoginVerifyUrl(appId, appSecret, jsCode);

        return url;
    }

    /**
     * 小程序登录校验信息
     *
     * @param jsCode
     * @return
     */
    public LoginVerifyInfo fetchLoginVerifyInfo(String jsCode) throws BizException {
        String appId = miniAppService.getAppId();

        String secret = miniAppService.getSecret();

        String fetchLoginVerifyInfoUrl = this.getfetchLoginVerifyUrl(appId, secret, jsCode);

        String response = HttpsHelper.get(fetchLoginVerifyInfoUrl);

        if (response == null || response.indexOf("errcode") != -1) {
            throw new BizException("小程序登录校验失败");
        }

        Log.i("==> fetchLoginVerifyInfo : response = {?} ", response);

        LoginVerifyInfo loginVerifyInfo = JsonUtil.readValue(response, LoginVerifyInfo.class);

        if (loginVerifyInfo != null) {
            Log.d("==> fetchLoginVerifyInfo:  session_key = {?} , openid = {?} ", loginVerifyInfo.getSessionKey(), loginVerifyInfo.getOpneId());
        }

        return loginVerifyInfo;
    }
}
