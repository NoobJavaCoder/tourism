package com.keendo.biz.service;

import com.keendo.wxpay.bean.PaySignature;
import com.keendo.wxpay.service.WXPayKitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/*支付订单*/
@Service
public class PayOrderService {
    @Autowired
    private WXPayKitService wxPayKitService;

    public PaySignature payOrder(){

        //PaySignature paySignature = wxPayKitService.pay();
        return null;
    }


}
