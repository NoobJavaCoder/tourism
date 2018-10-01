package com.keendo.wxpay.mapper;



import com.keendo.wxpay.model.PayRecord;

import java.util.List;

public interface PayRecordMapper {
    PayRecord selectById(Integer id);

    Integer insert(PayRecord record);

    Integer update(PayRecord record);

    List<PayRecord> selectByStatus(Integer status);

    PayRecord selectByOrderSn(String orderSn);
}
