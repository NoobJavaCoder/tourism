package com.keendo.biz.controller.app;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.architecture.exception.BizException;
import com.keendo.biz.controller.admin.bean.order.AddTourOrderReq;
import com.keendo.biz.controller.app.req.OrderPayReq;
import com.keendo.biz.controller.base.bean.IdReq;
import com.keendo.biz.controller.base.bean.PageParamReq;
import com.keendo.biz.model.TourOrder;
import com.keendo.biz.service.PayOrderService;
import com.keendo.biz.service.TourOrderService;
import com.keendo.biz.service.bean.order.MyOrderDetail;
import com.keendo.biz.service.bean.order.MyOrderItem;
import com.keendo.biz.service.bean.order.OrderUserDetail;
import com.keendo.user.controlller.BaseController;
import com.keendo.wxpay.bean.PaySignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by bint on 2018/10/11.
 */
@RestController
@RequestMapping(value = "/app/tour-order")
public class AppTourOrderController extends BaseController {

    @Autowired
    private TourOrderService tourOrderService;

    @Autowired
    private PayOrderService payOrderService;

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBase getAuctionItemPage(@RequestBody AddTourOrderReq addTourOrderReq) {

        Integer userId = getUserId();

        if (userId == null) {
            return RespHelper.nologin();
        }

        Integer productId = addTourOrderReq.getProductId();
        Integer orderId = tourOrderService.addOrder(userId, productId);

        return RespHelper.ok(orderId);
    }

    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public RespBase cancelOrder(@RequestBody IdReq idReq) {

        if (idReq == null) {
            throw new BizException("订单不能为空");
        }

        Integer tourOrderId = idReq.getId();
        if (tourOrderId == null) {
            throw new BizException("订单不能为空");
        }

        Integer userId = getUserId();
        if (userId == null) {
            return RespHelper.nologin();
        }

        tourOrderService.cancelOrder(tourOrderId, userId);

        return RespHelper.ok();
    }


    @ResponseBody
    @RequestMapping(value = "/my-list", method = RequestMethod.POST)
    public RespBase getMyOrderList(@RequestBody PageParamReq pageParamReq) {

        Integer userId = getUserId();
        if (userId == null) {
            return RespHelper.nologin();
        }

        Integer startIndex = pageParamReq.getStartIndex();

        Integer pageSize = pageParamReq.getPageSize();

        List<MyOrderItem> myOrderItemList = tourOrderService.getMyOrderList(userId, startIndex, pageSize);

        return RespHelper.ok(myOrderItemList);
    }

    @ResponseBody
    @RequestMapping(value = "/get-detail", method = RequestMethod.POST)
    public RespBase getMyOrderDetail(@RequestBody IdReq idReq) {

        Integer userId = getUserId();
        if (userId == null) {
            return RespHelper.nologin();
        }

        Integer id = idReq.getId();
        if (id == null) {
            return RespHelper.failed("订单不能为空");
        }

        TourOrder tourOrder = tourOrderService.getById(id);
        if (tourOrder == null) {
            return RespHelper.failed("找不到该订单");
        }

        if (!tourOrder.getUserId().equals(userId)) {
            return RespHelper.failed("不属于当前的用户订单");
        }

        MyOrderDetail myOrderDetail = tourOrderService.getMyOrderDetail(id);

        return RespHelper.ok(myOrderDetail);
    }


    /**
     * 订单付款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public RespBase orderPay(@RequestBody OrderPayReq orderPayReq) {

        Integer userId = getUserId();

        BigDecimal amount = orderPayReq.getAmount();

        String orderSn = orderPayReq.getOrderSn();

        PaySignature paySignature = payOrderService.payOrder(userId, amount, orderSn);

        return RespHelper.ok(paySignature);
    }
}
