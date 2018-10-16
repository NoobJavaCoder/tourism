package com.keendo.biz.controller.app;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.architecture.exception.BizException;
import com.keendo.biz.controller.admin.bean.order.AddTourOrderReq;
import com.keendo.biz.controller.base.bean.IdReq;
import com.keendo.biz.service.TourOrderService;
import com.keendo.biz.service.bean.order.OrderUserDetail;
import com.keendo.user.controlller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by bint on 2018/10/11.
 */
@RestController
@RequestMapping(value = "/app/tour-order")
public class AppTourOrderController extends BaseController{

    @Autowired
    private TourOrderService tourOrderService;

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBase getAuctionItemPage(@RequestBody AddTourOrderReq addTourOrderReq){

        Integer userId = getUserId();

        if(userId == null){
            return RespHelper.nologin();
        }

        OrderUserDetail orderUserDetail = addTourOrderReq.getOrderUserDetail();
        if(orderUserDetail == null){
            throw new BizException("用户不能为空");
        }
        Integer productId = addTourOrderReq.getProductId();
        Integer orderId = tourOrderService.addOrder(userId, orderUserDetail, productId);

        return RespHelper.ok(orderId);
    }

    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public RespBase cancelOrder(@RequestBody IdReq idReq){

        if(idReq == null ){
            throw new BizException("订单不能为空");
        }

        Integer tourOrderId = idReq.getId();
        if(tourOrderId == null){
            throw  new BizException("订单不能为空");
        }

        Integer userId = getUserId();
        if(userId == null){
            return RespHelper.nologin();
        }

        tourOrderService.cancelOrder(tourOrderId ,userId);

        return RespHelper.ok();
    }
}
