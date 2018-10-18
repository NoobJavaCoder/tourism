package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.user.model.User;
import com.keendo.user.service.UserService;
import com.keendo.wxpay.bean.PaySignature;
import com.keendo.wxpay.service.WXPayKitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/*支付订单*/
@Service
public class PayOrderService {
    @Autowired
    private WXPayKitService wxPayKitService;

    @Autowired
    private UserService userService;

    public PaySignature payOrder(Integer userId, BigDecimal amount, String orderSn) {

        //openid body amount orderSn
        String openId = this.getOpenIdByUserId(userId);

        String body = this.getPayBody();

        PaySignature paySignature = wxPayKitService.pay(openId, body, amount, orderSn);

        return paySignature;
    }

    /**
     * 根据用户id获取openId
     *
     * @param userId
     * @return
     */
    private String getOpenIdByUserId(Integer userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        String openId = user.getOpenId();
        return openId;
    }


    /**
     * TODO 暂时常量代替,测试后再确定是否需要修改
     * 商品描述
     *
     * @return
     */
    private String getPayBody() {
        return Constants.PAY_DESC_BODY;
    }

    private static class Constants {
        private final static String PAY_DESC_BODY = "金豆-旅游产品支付测试";
    }


}
