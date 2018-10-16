package com.keendo.biz.mapper;


import com.keendo.biz.model.OrderOpt;

import java.util.List;

/**
 * Created by bint on 2018/8/28.
 */
public interface OrderOptMapper {

    int insert(OrderOpt orderOpt);

    List<OrderOpt> selectByOrderId(Integer orderId);
}
