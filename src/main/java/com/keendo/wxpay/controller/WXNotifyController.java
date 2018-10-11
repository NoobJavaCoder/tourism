package com.keendo.wxpay.controller;

import com.keendo.wxpay.bean.PayCallBack;
import com.keendo.wxpay.bean.WXNotifyResp;
import com.keendo.wxpay.service.IPayResultService;
import com.keendo.wxpay.service.PayRecordService;
import com.keendo.wxpay.utils.WXPayUtil;
import com.keendo.wxpay.utils.XmlBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@Controller
@RequestMapping(value = "/wx-pay")
public class WXNotifyController {


    private static final String RESULT_CODE_SUCCESS = "SUCCESS";

    @Autowired
    IPayResultService payResultService;//对支付接口进行处理的方法

    @Autowired
    private PayRecordService payRecordService;


    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public void callback(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String xmlRet = WXPayUtil.readXml(request);

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
                fail(response);
            }

            /*以下为支付成功操作*/

            //修改订单状态
            payResultService.orderSuccess(orderNo);

            //修改支付记录状态并记录第三方流水号
            payRecordService.success(orderNo, thirdPartOrderNo);

            //是否有必要把回调信息单独记录下来?

            ok(response);

        } else {
            fail(response);
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
