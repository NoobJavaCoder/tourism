package com.keendo.user.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.architecture.utils.Log;
import com.keendo.user.model.User;
import com.keendo.user.service.bean.user.EncryptedUserinfo;
import com.keendo.user.service.bean.user.UserBaseInfo;
import com.keendo.user.service.helper.MiniAppHelper;
import com.keendo.user.service.helper.bean.LoginVerifyInfo;
import com.keendo.user.service.utils.AesCbcUtil;
import com.keendo.user.service.utils.BeanUtil;
import com.keendo.user.service.utils.JsonUtil;
import com.keendo.user.service.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MiniAppUserinfoService {

    @Autowired
    private UserService userService;

    @Autowired
    private MiniAppHelper miniAppHelper;

    @Autowired
    private CacheSessionService cacheSessionService;

    /**
     * 注册
     * 授权获取用户信息并存入数据库
     *
     * @param encryptedUserinfo:用户信息加密串
     * @return
     */
    public String register(EncryptedUserinfo encryptedUserinfo) throws BizException {

        //login
        String code = encryptedUserinfo.getCode();
        LoginVerifyInfo loginVerifyInfo = miniAppHelper.fetchLoginVerifyInfo(code);
        String sessionKey = loginVerifyInfo.getSessionKey();
        String token = RandomUtils.randomUUID();


        //userInfo
        String encryptedData = encryptedUserinfo.getEncryptedData();
        String iv = encryptedUserinfo.getIv();
        UserBaseInfo userBaseInfo = this.decodeUserInfo(sessionKey, encryptedData, iv);


        //saveOrUpdate
        String openId = userBaseInfo.getOpenId();
        User record = userService.getByOpenId(openId);

        Integer userId;

        if (record == null) {
            //save
            User user = BeanUtil.copyBean(userBaseInfo, User.class);
            user.setCreateTime(new Date());
            userId = userService.save(user);

        } else {
            //update
            BeanUtil.copyProperties(userBaseInfo, record);
            userService.update(record);
            userId = record.getId();
        }

        //set seesion
        cacheSessionService.setUserId(token, userId);

        return token;
    }


    /**
     * 解密用户敏感数据
     *
     * @param encryptedData 用户信息加密数据
     * @param iv            加密算法的初始向量,用于解密
     * @return:用户信息
     */
    private UserBaseInfo decodeUserInfo(String sessionKey, String encryptedData, String iv) {

        //解密用户信息加密串
        try {
            String result = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");

            if (null != result && result.length() > 0) {
                UserBaseInfo userBaseInfo = JsonUtil.readValue(result, UserBaseInfo.class);

                return userBaseInfo;
            }
        } catch (Exception e) {
            Log.e(e);
            e.printStackTrace();
        }

        return null;
    }


}

