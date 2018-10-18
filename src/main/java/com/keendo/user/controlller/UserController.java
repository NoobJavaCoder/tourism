package com.keendo.user.controlller;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.architecture.exception.BizException;
import com.keendo.user.controlller.req.LoginReq;
import com.keendo.user.service.MiniAppLoginService;
import com.keendo.user.service.MiniAppUserinfoService;
import com.keendo.user.service.UserService;
import com.keendo.user.service.bean.user.EncryptedUserinfo;
import com.keendo.user.service.bean.user.LoginRet;
import com.keendo.user.service.bean.user.UserInfoVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bint on 2018/9/10.
 */
@RequestMapping("/app/user")
@RestController
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    @Autowired
    MiniAppLoginService miniAppLoginService;

    @Autowired
    private MiniAppUserinfoService miniAppUserinfoService;



    /**
     * 小程序登陆
     *
     * @param loginReq
     * @return:登陆状态token
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RespBase login(@RequestBody LoginReq loginReq) {
        String jsCode = loginReq.getJsCode();

        String token = miniAppLoginService.login(jsCode);

        Map<String, String> ret = new HashMap<>();

        ret.put("token", token);

        return RespHelper.ok(ret);
    }

    /**
     * 检查用户是否登录
     * @return:登陆状态token
     */
    @RequestMapping(value = "/check-login", method = RequestMethod.POST)
    public RespBase isLogin() {

        Integer userId = getUserId();

        LoginRet ret = userService.isLogin(userId);

        return RespHelper.ok(ret);
    }

    /**
     * 授权注册
     *
     * @param encryptedUserinfo
     * @return:登陆状态token
     */
    @RequestMapping(value = "/auth-register", method = RequestMethod.POST)
    public RespBase auth(@RequestBody EncryptedUserinfo encryptedUserinfo) throws BizException {
        String token = miniAppUserinfoService.register(encryptedUserinfo);

        Map<String, String> ret = new HashMap<>();
        ret.put("token", token);

        return RespHelper.ok(ret);
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public RespBase getInfo() {
        Integer userId = getUserId();
        if (userId == null) {
            return RespHelper.nologin();
        }

        UserInfoVO userInfoVO = userService.getUserInfoVO(userId);

        return RespHelper.ok(userInfoVO);
    }


}
