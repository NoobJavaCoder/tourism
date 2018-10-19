package com.keendo.biz.service;

import com.keendo.biz.model.TourOrder;
import com.keendo.wxpay.service.IPayResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class PayResultService implements IPayResultService {

    @Autowired
    private TourOrderService tourOrderService;

    @Override
    public Boolean isOrderExist(String orderNo) {
        TourOrder tourOrder = tourOrderService.getByOrderSn(orderNo);

        if(tourOrder == null){
            return false;
        }
        return true;
    }

    @Override
    public void orderSuccess(String orderNo) {
        tourOrderService.paySuccess(orderNo);
    }

    @Override
    public BigDecimal getOrderFee(String orderNo) {
        BigDecimal fee = tourOrderService.getFeeByOrderId(orderNo);
        return fee;
    }

    @Override
    public Boolean isOrderPaid(String orderNo) {
        return tourOrderService.isOrderPaid(orderNo);
    }
}
