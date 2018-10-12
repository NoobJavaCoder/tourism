package com.keendo.wxpay.service;

import com.keendo.wxpay.bean.*;
import com.keendo.wxpay.constant.WXPayConstants;
import com.keendo.wxpay.exception.BizException;
import com.keendo.wxpay.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@Service
public class WXPayKitService {


    @Autowired
    IPayResultService payResultService;//对支付接口进行处理的方法

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    //查询订单url
    private static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    private static final String RESULT_CODE_SUCCESS = "SUCCESS";


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

    /**
     * 支付回调处理方法
     *
     * @param xmlRet
     * @return
     * @throws Exception
     */
    @Transactional
    public WXNotifyResp callback(String xmlRet) throws Exception {

        Boolean signValid = WXPayUtil.checkSign(xmlRet);

        String resultCode = WXPayUtil.getResultCode(xmlRet);

        //回调信息正确且验签成功
        if (signValid && RESULT_CODE_SUCCESS.equals(resultCode)) {

            PayCallBack payCallBack = XmlBeanUtil.toBeanWithCData(xmlRet, PayCallBack.class);

            Integer totalFee = payCallBack.getTotalFee();//回调支付金额

            String orderNo = payCallBack.getOutTradeNo();//回调系统订单号

            String thirdPartOrderNo = payCallBack.getTransactionId();//微信第三方支付流水号

            Boolean isOrderExist = payResultService.isOrderExist(orderNo);

            Boolean isOrderPaid = payResultService.isOrderPaid(orderNo);

            BigDecimal orderFee = payResultService.getOrderFee(orderNo);
            Boolean isValidFee = totalFee.equals((WXPayUtil.getTotalFee(orderFee)).intValue());

            //订单不存在 || 订单已支付 || 金额不匹配
            if (!isOrderExist || isOrderPaid || !isValidFee) {
                return WXNotifyResp.fail();
            }

            /*以下为支付成功操作,相同事务域*/

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    //修改订单状态
                    payResultService.orderSuccess(orderNo);

                    //修改支付记录状态并记录第三方流水号
                    payRecordService.success(orderNo, thirdPartOrderNo);

                    //save callback info
                }
            });

            return WXNotifyResp.ok();

        } else {

            return WXNotifyResp.fail();
        }
    }

    /**
     * 响应微信
     *
     * @param response
     * @throws Exception
     */
    private void ok(HttpServletResponse response) throws Exception {
        String xml = XmlBeanUtil.toXmlWithCData(WXNotifyResp.fail());
        WXPayUtil.writeXml(response, xml);
    }

    /**
     * 响应微信
     *
     * @param response
     * @throws Exception
     */
    private void fail(HttpServletResponse response) throws Exception {
        String xml = XmlBeanUtil.toXmlWithCData(WXNotifyResp.ok());
        WXPayUtil.writeXml(response, xml);
    }


}
