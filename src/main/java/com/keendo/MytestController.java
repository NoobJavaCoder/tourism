package com.keendo;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.model.UserInfo;
import com.keendo.biz.service.COSSmsService;
import com.keendo.biz.service.UserInfoService;
import com.keendo.biz.service.bean.userinfo.UserInfoResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class MytestController {

    @Autowired
    private COSSmsService smsService;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public RespBase test1() {
        UserInfo info = userInfoService.getByUserId(1);
        return RespHelper.ok(info);
    }


}
