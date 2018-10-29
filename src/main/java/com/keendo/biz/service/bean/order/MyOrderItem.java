package com.keendo.biz.service.bean.order;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by bint on 2018/10/16.
 */
public class MyOrderItem {

    private Integer orderId;
    private String productTitle;
    private String coverImgUrl;

    private Date departureTime;
    private BigDecimal price;

    private Integer tourOrderState;

    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getTourOrderState() {
        return tourOrderState;
    }

    public void setTourOrderState(Integer tourOrderState) {
        this.tourOrderState = tourOrderState;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
