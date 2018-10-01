package com.keendo.wxpay.service;

import com.keendo.wxpay.bean.OrderQueryReq;
import com.keendo.wxpay.bean.OrderQueryResp;
import com.keendo.wxpay.bean.UnifiedorderReq;
import com.keendo.wxpay.bean.UnifiedorderResp;
import com.keendo.wxpay.constant.WXPayConstants;
import com.keendo.wxpay.exception.BizException;
import com.keendo.wxpay.utils.*;
import org.springframework.stereotype.Service;

@Service
public class WXPayKitService {


    //查询订单url
    private static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";


    /**
     * 统一下单
     *
     * @return
     */
    public UnifiedorderResp unifiedOrder(UnifiedorderReq req) throws BizException {

        Log.i("【微信支付】发起支付，request={?}", JsonUtil.toJSon(req));

        String requestXml = XmlBeanUtil.toXml(req);

        String response = HttpUtil.sendPost(Configure.getNotifyUrl(), requestXml);

        UnifiedorderResp uoResp = XmlBeanUtil.toBeanWithCData(response, UnifiedorderResp.class);

        if (WXPayConstants.SUCCESS.equals(uoResp.getReturnCode()) && WXPayConstants.SUCCESS.equals(uoResp.getResultCode())) {

            return uoResp;
        } else {

            Log.e("unifiedOrder request fail , response = {?} ", response);
            throw new BizException("系统繁忙,请稍后再试");
        }

    }

    /**
     * 订单查询
     *
     * @param req
     * @return
     */
    public OrderQueryResp queryOrder(OrderQueryReq req) throws BizException {

        Log.i("【订单查询】，request={?}", JsonUtil.toJSon(req));

        String requestXml = XmlBeanUtil.toXml(req);

        String response = HttpUtil.sendPost(QUERY_ORDER_URL, requestXml);

        OrderQueryResp oqResp = XmlBeanUtil.toBeanWithCData(response, OrderQueryResp.class);

        if (WXPayConstants.SUCCESS.equals(oqResp.getReturnCode()) && WXPayConstants.SUCCESS.equals(oqResp.getResultCode())) {

            return oqResp;
        } else {

            Log.e("orderQuery request fail , response = {?} ", response);
            throw new BizException("系统繁忙,请稍后再试");
        }

    }


}
