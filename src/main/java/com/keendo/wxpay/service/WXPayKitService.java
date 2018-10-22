package com.keendo.wxpay.service;

import com.keendo.wxpay.bean.*;
import com.keendo.wxpay.exception.BizException;
import com.keendo.wxpay.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

@Service
public class WXPayKitService {


    @Autowired
    private IPayResultService payResultService;//对支付接口进行处理的方法

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private IMiniAppConfigService miniAppPayConfigService;



    /**
     * 生成调用微信统一下单接口的请求参数
     *
     * @param openId:付款方用户openId
     * @param body:支付信息
     * @param amount:金额(传入金额单位为元)
     * @param orderSn:系统订单编号
     * @return
     * @throws Exception
     */
    public UnifiedorderParam createUnifiedorderParam(String openId, String body, BigDecimal amount, String orderSn) {
        UnifiedorderParam unifiedorderParam = new UnifiedorderParam();

        String appId = miniAppPayConfigService.getAppId();
        unifiedorderParam.setAppId(appId);

        String mchId = miniAppPayConfigService.getMchId();
        unifiedorderParam.setMchId(mchId);

        String nonceStr = WXPayUtil.createNonceStr();
        unifiedorderParam.setNonceStr(nonceStr);

        unifiedorderParam.setOpenId(openId);

        String payNotifyUrl = miniAppPayConfigService.getPayNotifyUrl();
        unifiedorderParam.setNotifyUrl(payNotifyUrl);

        unifiedorderParam.setOutTradeNo(orderSn);

        unifiedorderParam.setTradeType("JSAPI");

        unifiedorderParam.setBody(body);

        Integer totalFee = WXPayUtil.getTotalFee(amount);
        unifiedorderParam.setTotalFee(totalFee);

        String createIp = WXPayUtil.getCreateIp();
        unifiedorderParam.setSpBillCreateIp(createIp);

        String key = miniAppPayConfigService.getMchKey();
        String sign = WXPayUtil.getSign(unifiedorderParam, key);
        unifiedorderParam.setSign(sign);

        return unifiedorderParam;
    }

    /**
     * 统一下单
     *
     * @return
     */
    public UnifiedorderResp unifiedOrder(UnifiedorderParam param) throws BizException {

        Log.i("【WX Pay】，request = {?}", JsonUtil.toJSon(param));

        String requestXml = XmlBeanUtil.toXml(param);

        String response = HttpUtil.sendPost(Constants.UNIFIED_ORDER_URL, requestXml);

        System.out.println(response);

        Log.i("【WX Pay】，response = {?}", response);

        UnifiedorderResp uoResp = XmlBeanUtil.toBeanWithCData(response, UnifiedorderResp.class);

        if (Constants.RETURN_CODE_SUCCESS.equals(uoResp.getReturnCode()) && Constants.RESULT_CODE_SUCCESS.equals(uoResp.getResultCode())) {

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

        Log.i("【OrderQuery】，request={?}", JsonUtil.toJSon(req));

        String requestXml = XmlBeanUtil.toXml(req);

        String response = HttpUtil.sendPost(Constants.QUERY_ORDER_URL, requestXml);

        Log.i("【OrderQuery】, response = {?}", response);

        OrderQueryResp orderQueryResp = XmlBeanUtil.toBeanWithCData(response, OrderQueryResp.class);

        if (Constants.RETURN_CODE_SUCCESS.equals(orderQueryResp.getReturnCode()) && Constants.RESULT_CODE_SUCCESS.equals(orderQueryResp.getResultCode())) {

            return orderQueryResp;
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
    public WXNotifyResp callback(String xmlRet) {
        String key = miniAppPayConfigService.getMchKey();

        Boolean signValid = WXPayUtil.checkSign(xmlRet, key);

        String resultCode = WXPayUtil.getResultCode(xmlRet);

        //回调信息正确且验签成功
        if (signValid && Constants.RESULT_CODE_SUCCESS.equals(resultCode)) {

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
     * 生成小程序支付参数对象,传递给小程序端拉起微信支付
     *
     * @return
     */
    public  MiniAppPayParam createMiniAppPayParam(String appId, String nonceStr, String prepayId) {
        PaySignature paySignature = new PaySignature(appId, nonceStr, prepayId);

        MiniAppPayParam miniAppPayParam = BeanUtil.copyBean(paySignature, MiniAppPayParam.class);

        String key = miniAppPayConfigService.getMchKey();

        String paySign = WXPayUtil.getSign(paySignature, key);

        miniAppPayParam.setPaySign(paySign);

        return miniAppPayParam;
    }



    public static class Constants {
        private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单api地址

        private static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";//查询订单url

        private static final String RESULT_CODE_SUCCESS = "SUCCESS";

        private static final String RETURN_CODE_SUCCESS = "SUCCESS";

        public static final String WX_TRADE_STATE_SUCCESS = "SUCCESS";
    }


}
