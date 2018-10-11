package com.keendo.biz.service;

import com.keendo.wxpay.service.IPayResultService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class PayResultService implements IPayResultService {
    @Override
    public Boolean isOrderExist(String orderNo) {
        return null;
    }

    @Override
    public void orderSuccess(String orderNo) {

    }

    @Override
    public BigDecimal getOrderFee(String orderNo) {
        return null;
    }

    @Override
    public Boolean isOrderPaid(String orderNo) {
        return null;
    }
}
