package com.keendo.wxpay.service;

import com.keendo.wxpay.bean.MiniAppPayParam;
import com.keendo.wxpay.bean.UnifiedorderParam;
import com.keendo.wxpay.bean.UnifiedorderResp;
import com.keendo.wxpay.model.PayRecord;
import com.keendo.wxpay.utils.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class WXPayService {


    @Autowired
    private WXPayKitService wxPayKitService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private IMiniAppConfigService miniAppPayConfigService;
    /**
     * 支付
     *
     * @param openId:付款方用户openId
     * @param body:支付信息,商品描述   例:腾讯充值中心-QQ会员充值
     * @param amount:金额(传入金额单位为元)
     * @param orderSn:系统订单编号(32位以内,系统唯一,字母数字的字符串)
     * @return : 小程序端拉起支付需要的参数
     */
    public MiniAppPayParam pay(String openId, String body, BigDecimal amount, String orderSn) {

        UnifiedorderParam unifiedorderParam = wxPayKitService.createUnifiedorderParam(openId, body, amount, orderSn);

        MiniAppPayParam miniAppPayParam = transactionTemplate.execute(new TransactionCallback<MiniAppPayParam>() {
            @Override
            public MiniAppPayParam doInTransaction(TransactionStatus transactionStatus) {

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
                UnifiedorderResp unifiedorderResp = wxPayKitService.unifiedOrder(unifiedorderParam);
                String prepayId = unifiedorderResp.getPrepayId();
                PayRecord pr = payRecordService.getById(recordId);
                pr.setPrepayId(prepayId);
                payRecordService.update(pr);

                //2 构建返回参数
                String appId = miniAppPayConfigService.getAppId();
                String nonceStr = WXPayUtil.createNonceStr();

                MiniAppPayParam miniAppPayParam = wxPayKitService.createMiniAppPayParam(appId, nonceStr, prepayId);

                return miniAppPayParam;
            }
        });

        return miniAppPayParam;
    }
}
