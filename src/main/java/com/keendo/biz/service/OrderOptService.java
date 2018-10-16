package com.keendo.biz.service;

import com.keendo.biz.mapper.OrderOptMapper;
import com.keendo.biz.model.OrderOpt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by bint on 2018/8/28.
 */
@Service
public class OrderOptService {

    @Autowired
    private OrderOptMapper orderOptMapper;

    private Integer save(OrderOpt orderOpt){

        return orderOptMapper.insert(orderOpt);
    }

    public Integer add(Integer orderId ,Integer fromState ,Integer toState ,Integer userId){

        OrderOpt orderOpt = new OrderOpt();

        orderOpt.setCreateTime(new Date());
        orderOpt.setFromState(fromState);
        orderOpt.setToState(toState);
        orderOpt.setOrderId(orderId);
        orderOpt.setUserId(userId);

        return this.save(orderOpt);
    }

    public Integer addSystemOpt(Integer orderId ,Integer fromState ,Integer toState ){

        return add(orderId, fromState, toState , Constant.SYSTEM_USER);
    }

    /**
     * 获取指定订单的操作记录
     * @param orderId
     * @return
     */
    public List<OrderOpt> getListByOrderId(Integer orderId){
        return orderOptMapper.selectByOrderId(orderId);
    }


    private static class Constant{
        private static final Integer SYSTEM_USER = -1;
    }
}
