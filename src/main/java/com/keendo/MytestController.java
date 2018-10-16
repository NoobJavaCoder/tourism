package com.keendo;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.service.COSSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class MytestController {

    @Autowired
    private COSSmsService smsService;

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public RespBase test1() {

        smsService.sendMsg("18826277525",1,"662407");
        return RespHelper.ok();
    }


}
