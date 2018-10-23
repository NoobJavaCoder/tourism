package com.keendo.biz.schdule;

import com.keendo.architecture.utils.Log;
import com.keendo.biz.service.TourOrderService;
import com.keendo.biz.service.TourProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Async
@Component
public class ScheduledService {

    @Autowired
    private TourProductService tourProductService;

    @Autowired
    private TourOrderService tourOrderService;

    /**
     * 修改到达旅行结束时间的产品的状态
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void reviseEndStateTourProduct() {
        Log.i("task : revise end state for tour product ");
        tourProductService.reviseEndState();
    }

    /**
     * 修改到达报名截止时间的产品的状态
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void reviseDeadlineStateTourProduct() {
        Log.i("task : revise dealine state for tour product");
        tourProductService.reviseDeadlineState();
    }

    /**
     * 关闭未付款的订单
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void closeUnPaidTourOrder() {
        tourOrderService.cancelUnPaidTourOrder();
    }

}
