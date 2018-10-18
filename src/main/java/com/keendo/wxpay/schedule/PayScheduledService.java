package com.keendo.wxpay.schedule;

import com.keendo.wxpay.service.IMiniAppConfigService;
import com.keendo.wxpay.bean.OrderQueryReq;
import com.keendo.wxpay.bean.OrderQueryResp;
import com.keendo.wxpay.model.PayRecord;
import com.keendo.wxpay.service.IPayResultService;
import com.keendo.wxpay.service.PayRecordService;
import com.keendo.wxpay.service.WXPayKitService;
import com.keendo.wxpay.utils.Log;
import com.keendo.wxpay.utils.WXPayUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/*默认单线程,使用多线程,多个定时任务是异步执行*/
@Async
@Component
public class PayScheduledService {

    @Resource
    private PayRecordService payRecordService;

    @Resource
    private WXPayKitService wxPayKitService;

    @Resource
    private IMiniAppConfigService miniAppPayConfigService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IPayResultService payResultService;

    /**
     * 每分钟执行一次
     * 检查订单支付结果
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void checkPayStatus() {
        Log.i("Task order query begin");

        List<PayRecord> notPay = payRecordService.getNotPayRecordList();
        if (CollectionUtils.isEmpty(notPay)) {
            return;
        }
        for (PayRecord record : notPay) {
            OrderQueryReq req = new OrderQueryReq();

            String appId = miniAppPayConfigService.getAppId();
            req.setAppId(appId);

            String mchId = miniAppPayConfigService.getMchId();
            req.setMchId(mchId);

            String nonceStr = WXPayUtil.createNonceStr();
            req.setNonceStr(nonceStr);

            String orderSn = record.getOrderSn();//系统订单号
            req.setOutTradeNo(orderSn);

            String key = miniAppPayConfigService.getMchKey();
            String sign = WXPayUtil.getSign(req, key);

            req.setSign(sign);

            OrderQueryResp orderQueryResp = wxPayKitService.queryOrder(req);

            //根据返回交易状态修改支付记录结果
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                    //修改订单状态
                    payResultService.orderSuccess(orderSn);

                    //修改支付记录状态并记录第三方流水号
                    String thirdPartOrderNo = orderQueryResp.getTransactionId();
                    payRecordService.success(orderSn, thirdPartOrderNo);

                }
            });

        }

    }

}
