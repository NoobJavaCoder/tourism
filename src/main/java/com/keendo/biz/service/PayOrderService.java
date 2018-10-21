package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.biz.model.TourOrderDetail;
import com.keendo.user.model.User;
import com.keendo.user.service.UserService;
import com.keendo.wxpay.bean.MiniAppPayParam;
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

    @Autowired
    private TourOrderDetailService tourOrderDetailService;


    /**
     * 付款
     *
     * @param userId:用户id
     * @return:小程序拉起支付需要参数
     */
    public MiniAppPayParam payOrder(Integer userId, Integer orderId) {

        TourOrderDetail orderDetail = tourOrderDetailService.getByOrderId(orderId);

        if (orderDetail == null) {
            throw new BizException("订单不存在");
        }

        BigDecimal price = orderDetail.getPrice();

        String orderSn = orderDetail.getOrderSn();

        String openId = this.getOpenIdByUserId(userId);

        String body = this.getPayBody();

        MiniAppPayParam miniAppPayParam = wxPayKitService.pay(openId, body, price, orderSn);

        return miniAppPayParam;
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
     * TODO 微信对body的描述欠缺,暂时常量代替,测试后再与产品商议如何修改
     * 商品描述
     *
     * @return
     */
    private String getPayBody() {
        return Constants.PAY_DESC_BODY;
    }

    private static class Constants {
        private final static String PAY_DESC_BODY = "旅游产品支付";
    }


}
