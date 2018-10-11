package com.keendo.biz.controller.admin.bean.order;

import com.keendo.biz.service.bean.order.OrderUserDetail;

/**
 * Created by bint on 2018/10/11.
 */
public class AddTourOrderReq {
    private OrderUserDetail orderUserDetail;
    private Integer productId;

    public OrderUserDetail getOrderUserDetail() {
        return orderUserDetail;
    }

    public void setOrderUserDetail(OrderUserDetail orderUserDetail) {
        this.orderUserDetail = orderUserDetail;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
