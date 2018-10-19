package com.keendo.biz.model;

import java.util.Date;

//表名e_tour_order
public class TourOrder {
    private Integer id;
    private Integer tourProductId;
    private Date createTime;
    private Integer state;
    private Integer userId;
    private String orderSn;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTourProductId() {
        return tourProductId;
    }

    public void setTourProductId(Integer tourProductId) {
        this.tourProductId = tourProductId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}