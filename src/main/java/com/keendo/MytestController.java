package com.keendo;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.service.COSSmsService;
import com.keendo.biz.service.TourProductService;
import com.keendo.biz.service.UserIdempotentService;
import com.keendo.biz.service.UserInfoService;
import com.keendo.biz.service.bean.tourproduct.TourProductItem;
import com.keendo.biz.service.bean.tourproduct.TourProductItemDetail;
import com.keendo.wxpay.model.PayRecord;
import com.keendo.wxpay.service.PayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RequestMapping("/test")
@RestController
public class MytestController {

    @Autowired
    private COSSmsService smsService;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TourProductService tourProductService;

    @Autowired
    private UserIdempotentService userIdempotentService;

    @Autowired
    private PayRecordService payRecordService;

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public RespBase test1() {
//        PayRecord pr = new PayRecord();
//        pr.setId(1);
//        pr.setPrepayId("123");
//        pr.setOrderSn("312");
//        pr.setStatus(PayRecordService.Constant.NOT_PAY);
//        pr.setTransactionId("456");
//        pr.setUpdateTime(new Date());
//        pr.setCreateTime(new Date());
//        pr.setAmount(new BigDecimatl("100"));
//        pr.setOpenId("asdhi32xx");
//        payRecordService.update(pr);
        TourProductItemDetail appTourProductDetail = tourProductService.getAppTourProductDetail(3);
        return RespHelper.ok(appTourProductDetail);
    }


}
