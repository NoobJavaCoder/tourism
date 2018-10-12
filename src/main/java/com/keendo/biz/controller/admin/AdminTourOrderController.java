package com.keendo.biz.controller.admin;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.architecture.exception.BizException;
import com.keendo.biz.controller.admin.bean.order.TourOrderPageReq;
import com.keendo.biz.controller.base.bean.IdReq;
import com.keendo.biz.service.TourOrderService;
import com.keendo.biz.service.bean.order.AdminProductOrderItemResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bint on 2018/10/11.
 */
@RestController
@RequestMapping(value = "/admin/tour-order")
public class AdminTourOrderController {

    @Autowired
    private TourOrderService tourOrderService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public RespBase getAuctionItemPage(@RequestBody TourOrderPageReq tourOrderPageReq){

        Integer tourProductId = tourOrderPageReq.getTourProductId();
        Integer pageSize = tourOrderPageReq.getPageSize();
        Integer startIndex = tourOrderPageReq.getStartIndex();

        List<AdminProductOrderItemResp> productOrderItemRespList = tourOrderService.getByTourProductIdPage(tourProductId, startIndex, pageSize);

        Integer count = tourOrderService.countByTourProductId(tourProductId);

        return RespHelper.ok(productOrderItemRespList ,count);
    }



}
