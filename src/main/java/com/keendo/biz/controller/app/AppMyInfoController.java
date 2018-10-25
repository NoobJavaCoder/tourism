package com.keendo.biz.controller.app;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.controller.app.req.SaveUserInfoReq;
import com.keendo.biz.controller.app.req.SendCodeReq;
import com.keendo.biz.model.UserInfo;
import com.keendo.biz.service.PhoneVerificationCodeService;
import com.keendo.biz.service.PhoneVerificationCodeService.Constants;
import com.keendo.biz.service.UserInfoService;
import com.keendo.biz.service.bean.userinfo.UserInfoResp;
import com.keendo.biz.service.utils.BeanUtils;
import com.keendo.user.controlller.BaseController;
import com.keendo.user.service.utils.VerifyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/app/my-info")
public class AppMyInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private PhoneVerificationCodeService phoneVerificationCodeService;

    /**
     * 保存个人信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RespBase saveUserInfo(@RequestBody SaveUserInfoReq req){
        Integer userId = getUserId();
        if(userId == null){
            return RespHelper.nologin();
        }

        UserInfo userInfo = BeanUtils.copyBean(req, UserInfo.class);

        String phoneNo = userInfo.getPhoneNo();
//        if(!VerifyUtil.isPhone(phoneNo)){
//            return RespHelper.failed("请填写正确的手机号码!");
//        }

        String verifyCode = req.getVerifyCode();
        if(!phoneVerificationCodeService.verifyUsable(phoneNo,verifyCode,Constants.MY_INFO_PHONE_TYPE)){
            return RespHelper.failed("验证码错误");
        }

//        String realName = userInfo.getRealName();
//        if(!VerifyUtil.isRealName(realName)){
//            return RespHelper.failed("请填写完整的真实姓名!");
//        }
//
//        String idCardNo = userInfo.getIdCardNo();
//        if(!VerifyUtil.isIdCard(idCardNo)){
//            return RespHelper.failed("请填写正确的身份证号码!");
//        }

        userInfo.setUserId(userId);

        userInfoService.saveUserInfo(userInfo);

        return RespHelper.ok();
    }

    /**
     * 获取个人信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public RespBase getUserInfo(){

        Integer userId = getUserId();
        if(userId == null){
            return RespHelper.nologin();
        }

        UserInfoResp resp = userInfoService.getAppUserInfo(userId);

        return RespHelper.ok(resp);
    }

    /**
     * 发送短信验证码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public RespBase sendCode(@RequestBody SendCodeReq sendCodeReq){

        String phoneNumber = sendCodeReq.getPhoneNumber();

        if(!VerifyUtil.isPhone(phoneNumber)){
            return RespHelper.failed("请填写正确的手机号码");
        }

        phoneVerificationCodeService.addMyInofVerificationCode(phoneNumber);
        return RespHelper.ok();
    }

}
