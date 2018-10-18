package com.keendo.wxpay.service;

import com.keendo.wxpay.mapper.PayRecordMapper;
import com.keendo.wxpay.model.PayRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayRecordService {

    @Autowired
    private PayRecordMapper payRecordMapper;


    /**
     * 获取所有待支付状态的支付记录
     *
     * @return
     */
    public List<PayRecord> getNotPayRecordList() {
        return payRecordMapper.selectByStatus(Constant.NOT_PAY);
    }

    /**
     * 根据系统订单号获取支付记录
     *
     * @param orderSn
     * @return
     */
    public PayRecord getByOrderSn(String orderSn) {
        return payRecordMapper.selectByOrderSn(orderSn);
    }

    /**
     * 根据支付记录id获取对象
     * @param id
     * @return
     */
    public PayRecord getById(Integer id){
        return payRecordMapper.selectById(id);
    }

    /**
     * 新增
     * @param payRecord
     * @return:新增对象主键
     */
    public Integer add(PayRecord payRecord) {
        payRecordMapper.insert(payRecord);
        return payRecord.getId();
    }

    public void update(PayRecord payRecord) {
        payRecordMapper.update(payRecord);
    }

    /**
     * 支付成功操作
     *
     * @param orderSn:订单号
     * @param transactionId:微信第三方支付流水号
     */
    public void success(String orderSn, String transactionId) {
        PayRecord payRecord = getByOrderSn(orderSn);
        payRecord.setTransactionId(transactionId);
        payRecord.setStatus(Constant.SUCCESS_PAY);
        this.update(payRecord);
    }

    public static class Constant {
        public static final Integer NOT_PAY = 0;//待付款
        public static final Integer FAIL_PAY = -1;//付款失败
        public static final Integer SUCCESS_PAY = 1;//付款成功
    }
}
