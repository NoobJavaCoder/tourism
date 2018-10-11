package com.keendo.wxpay.service;

import java.math.BigDecimal;

public interface IPayResultService {

    /**
     * 查询订单是否存在
     * @param orderNo:订单号
     * @return
     */
    Boolean isOrderExist(String orderNo);

    /**
     * 付款成功,修改订单为已付款状态
     * @param orderNo:订单号
     */
    void orderSuccess(String orderNo);

    /**
     * 查询订单的付款金额
     * @param orderNo:订单号
     * @return
     */
    BigDecimal getOrderFee(String orderNo);


    /**
     * 订单是否为已支付状态
     * @param orderNo
     * @return
     */
    Boolean isOrderPaid(String orderNo);

}
