package com.keendo.biz.controller.app.req;

import java.math.BigDecimal;

/**
 * 订单支付请求参数
 */
public class OrderPayReq {
    private String orderSn;
    private BigDecimal amount = new BigDecimal("0");

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
