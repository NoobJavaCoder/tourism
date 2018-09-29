package com.keendo.user.service;


import com.keendo.architecture.exception.BizException;
import com.keendo.architecture.utils.Log;
import com.keendo.user.service.helper.MiniAppHelper;
import com.keendo.user.service.helper.bean.LoginVerifyInfo;
import com.keendo.user.service.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiniAppLoginService {
    @Autowired
    private UserService userService;

    @Autowired
    private MiniAppHelper miniAppHelper;

    @Autowired
    private CacheSessionService cacheSessionService;

    /**
     * 小程序登陆
     * @param jsCode:登陆code
     * @return:token
     */
    public String login(String jsCode) throws BizException {
        //调用wx api获取登陆校验信息
        LoginVerifyInfo loginVerifyInfo = miniAppHelper.fetchLoginVerifyInfo(jsCode);

        String openId = loginVerifyInfo.getOpneId();

        String sessionKey = loginVerifyInfo.getSessionKey();

        String token = CommonUtils.randomUUID();

        Log.i("==> mini login : openid = {?} , session_key = {?} , token = {?} ", openId, sessionKey, token);

        boolean isExist = userService.isExist(openId);

        if (!isExist) {
            throw new BizException("尚未授权");
        }

        Integer userId = userService.getByOpenId(openId).getId();

        //set session
        //AppLoginUtil.setUserId(token, userId);
        cacheSessionService.setUserId(token,userId);

        return token;
    }
}
