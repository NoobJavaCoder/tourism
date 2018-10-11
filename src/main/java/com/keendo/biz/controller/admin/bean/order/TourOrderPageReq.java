package com.keendo.biz.controller.admin.bean.order;

import com.keendo.biz.controller.base.bean.PageParamReq;

/**
 * Created by bint on 2018/10/11.
 */
public class TourOrderPageReq extends PageParamReq{
    private Integer tourProductId;

    public Integer getTourProductId() {
        return tourProductId;
    }

    public void setTourProductId(Integer tourProductId) {
        this.tourProductId = tourProductId;
    }
}
