package com.keendo.wxpay.schedule;

import com.keendo.wxpay.service.PayRecordService;
import com.keendo.wxpay.service.WXPayKitService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/*默认单线程,使用多线程,多个定时任务是异步执行*/
@Async
@Component
public class PayScheduledService {

    @Resource
    private PayRecordService payRecordService;

    @Resource
    private WXPayKitService wxPayKitService;

    /**
     * 每分钟执行一次
     * 检查订单支付结果
     */
//    @Scheduled(cron = "0 0/1 * * * ? ")
//    public void checkPayStatus() {
//
//        List<PayRecord> notPay = payRecordService.getNotPayRecordList();
//        if(CollectionUtils.isEmpty(notPay)){
//            return;
//        }
//        for(PayRecord record:notPay){
//            OrderQueryReq req = new OrderQueryReq();
//            req.setAppId(Configure.getAppID());
//            req.setMchId(Configure.getMchId());
//            req.setNonceStr(WXPayUtil.createNonceStr());
//            req.setOutTradeNo(record.getOrderSn());
//
//            String sign = null;
//            try{
//                sign = WXPayUtil.getSign(req);
//            }catch (Exception e){
//                Log.e (e);
//            }
//            req.setSign(sign);
//
//            OrderQueryResp orderQueryResp = wxPayKitService.queryOrder(req);
//
//            //根据返回交易状态修改支付记录结果
//
//        }
//
//    }

}
