package com.keendo.biz.mapper;

import com.keendo.biz.model.TourOrderDetail;

/**
 * Created by bint on 2018/10/10.
 */
public interface TourOrderDetailMapper {

    int insert(TourOrderDetail tourOrderDetail);

    TourOrderDetail selectByOrderId(Integer orderId);
}
