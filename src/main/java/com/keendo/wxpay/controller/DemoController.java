package com.keendo.wxpay.controller;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.user.controlller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试demo
 */
@RequestMapping("/pay")
@RestController
public class DemoController extends BaseController {

    /**
     * TODO
     * 统一下单
     * @return
     */
    @RequestMapping(value = "/unified-order", method = RequestMethod.POST)
    public RespBase unifiedorder() {

        Integer userId = getUserId();
        if (userId == null) {
            return RespHelper.nologin();
        }


        return RespHelper.ok();
    }

    /**
     * TODO
     * 回调通知
     * @return
     */
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public RespBase callback() {

        Integer userId = getUserId();
        if (userId == null) {
            return RespHelper.nologin();
        }


        return RespHelper.ok();
    }
}
