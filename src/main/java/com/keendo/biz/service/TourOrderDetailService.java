package com.keendo.biz.service;

import com.keendo.biz.mapper.TourOrderDetailMapper;
import com.keendo.biz.model.TourOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bint on 2018/10/10.
 */
@Service
public class TourOrderDetailService {

    @Autowired
    private TourOrderDetailMapper tourOrderDetailMapper;

    public Integer save(TourOrderDetail tourOrderDetail){
        return tourOrderDetailMapper.insert(tourOrderDetail);
    }

    public TourOrderDetail getByOrderId(Integer orderId){
        return tourOrderDetailMapper.selectByOrderId(orderId);
    }
}
