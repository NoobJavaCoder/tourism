package com.keendo.biz.schdule;

import com.keendo.biz.model.TourProduct;
import com.keendo.biz.service.TourProductService;
import com.keendo.biz.service.utils.CommonUtils;
import com.keendo.biz.service.utils.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Async
@Component
public class ScheduledService {

    @Autowired
    private TourProductService tourProductService;

    /**
     * 每分钟执行一次
     * 旅游产品状态检查并修改
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void changeTourProductState(){

        List<TourProduct> tourProducts = tourProductService.getOnGoingStateList();

        if(ListUtil.isNotEmpty(tourProducts)){

            Iterator<TourProduct> iter = tourProducts.iterator();

            while(iter.hasNext()){

                TourProduct tp = iter.next();

                Integer id = tp.getId();//产品id

                Date departureTime = CommonUtils.dateStartTime(tp.getDepartureTime());//产品时间

                Integer tourDay = tp.getTourDay();//旅行天数

                Date endTime = CommonUtils.dateOffset(departureTime, tourDay);//结束时间

                Date now = new Date();//当前时间

                if(now.compareTo(endTime) >= 0){//超过结束时间

                    tourProductService.updateStateById(id,TourProductService.Constants.FINISH_STATE);

                }
            }
        }
    }

}
