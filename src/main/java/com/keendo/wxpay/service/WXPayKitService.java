package com.keendo.wxpay.service;

import com.keendo.wxpay.bean.*;
import com.keendo.wxpay.constant.WXPayConstants;
import com.keendo.wxpay.exception.BizException;
import com.keendo.wxpay.model.PayRecord;
import com.keendo.wxpay.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;

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
     * 支付
     *
     * @param openId:付款方用户openId
     * @param body:支付信息,商品描述 例:腾讯充值中心-QQ会员充值
     * @param amount:金额(传入金额单位为元)
     * @param orderSn:系统订单编号(32位以内,系统唯一,字母数字的字符串)
     */
    public PaySignature pay(String openId, String body, BigDecimal amount, String orderSn) throws Exception {

        UnifiedorderParam unifiedorderParam = this.createUnifiedorderParam(openId, body, amount, orderSn);

        PaySignature paySignature = transactionTemplate.execute(new TransactionCallback<PaySignature>() {
            @Override
            public PaySignature doInTransaction(TransactionStatus transactionStatus) {

                // 支付记录表中新增一条支付记录
                PayRecord payRecord = new PayRecord();

                payRecord.setAmount(amount);
                payRecord.setOpenId(openId);
                payRecord.setOrderSn(orderSn);
                payRecord.setStatus(PayRecordService.Constant.NOT_PAY);
                payRecord.setCreateTime(new Date());
                payRecord.setUpdateTime(new Date());

                Integer recordId = payRecordService.add(payRecord);


                // 调用统一下单接口
                // 1 更新支付记录对象,设置预支付id,用于后续发送模板消息
                UnifiedorderResp unifiedorderResp = unifiedOrder(unifiedorderParam);
                String prepayId = unifiedorderResp.getPrepayId();
                PayRecord pr = payRecordService.getById(recordId);
                pr.setPrepayId(prepayId);
                payRecordService.update(pr);

                //2 构建返回参数
                String appId = miniAppPayConfigService.getAppId();
                String nonceStr = WXPayUtil.createNonceStr();
                PaySignature paySignature = new PaySignature(appId, nonceStr, prepayId);

                return paySignature;
            }
        });

        return paySignature;
    }

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
    private UnifiedorderParam createUnifiedorderParam(String openId, String body, BigDecimal amount, String orderSn) throws Exception {
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

        String createIp = WXPayUtil.getCreateIp();
        unifiedorderParam.setSpBillCreateIp(createIp);

        unifiedorderParam.setBody(body);

        String totalFee = String.valueOf(WXPayUtil.getTotalFee(amount));
        unifiedorderParam.setTotalFee(totalFee);

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

        Log.i("【OrderQuery】，request={?}", JsonUtil.toJSon(req));

        String requestXml = XmlBeanUtil.toXml(req);

        String response = HttpUtil.sendPost(Constants.QUERY_ORDER_URL, requestXml);

        Log.i("【OrderQuery】, response = {?}",response);

        OrderQueryResp orderQueryResp = XmlBeanUtil.toBeanWithCData(response, OrderQueryResp.class);

        if (WXPayConstants.SUCCESS.equals(orderQueryResp.getReturnCode()) && WXPayConstants.SUCCESS.equals(orderQueryResp.getResultCode())) {

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
    @Transactional
    public WXNotifyResp callback(String xmlRet) throws Exception {
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

    private static class Constants {
        private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";//统一下单api地址

        private static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";//查询订单url

        private static final String RESULT_CODE_SUCCESS = "SUCCESS";
    }



    public static void main(String []args){
        String resp = "<xml><return_code><![CDATA[SUCCESS]]></return_code>" +
                "<return_msg><![CDATA[OK]]></return_msg>" +
                "<appid><![CDATA[wx2d547e92d59aefca]]></appid>" +
                "<mch_id><![CDATA[1500042702]]></mch_id>" +
                "<nonce_str><![CDATA[onVpD55LFJa4CVlI]]></nonce_str>" +
                "<sign><![CDATA[E2C49A90EE4EA2ECA9686005801819A0]]></sign>" +
                "<result_code><![CDATA[FAIL]]></result_code>" +
                "<err_code><![CDATA[ORDERNOTEXIST]]></err_code>" +
                "<err_code_des><![CDATA[order not exist]]></err_code_des>" +
                "</xml>";

        OrderQueryResp orderQueryResp = XmlBeanUtil.toBeanWithCData(resp, OrderQueryResp.class);
        System.out.println(orderQueryResp);
    }
}
