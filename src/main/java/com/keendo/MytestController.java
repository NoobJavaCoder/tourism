package com.keendo;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.service.COSSmsService;
import com.keendo.biz.service.TourProductService;
import com.keendo.biz.service.UserIdempotentService;
import com.keendo.biz.service.UserInfoService;
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
    @Autowired
    private TourProductService tourProductService;

    @Autowired
    private UserIdempotentService userIdempotentService;

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public RespBase test1() {
        Integer add = userIdempotentService.add(1);
        return RespHelper.ok();
    }


}
