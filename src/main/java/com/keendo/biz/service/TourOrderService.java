package com.keendo.biz.service;

import com.keendo.biz.mapper.TourOrderMapper;
import com.keendo.biz.model.TourOrder;
import com.keendo.biz.model.TourOrderDetail;
import com.keendo.biz.model.TourProduct;
import com.keendo.biz.service.bean.order.AdminProductOrderItemResp;
import com.keendo.user.model.User;
import com.keendo.user.service.UserService;
import com.keendo.user.service.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import com.keendo.biz.service.bean.order.OrderUserDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bint on 2018/10/10.
 */
@Service
public class TourOrderService {

    @Autowired
    private TourOrderMapper tourOrderMapper;

    @Autowired
    private TourOrderDetailService tourOrderDetailService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TourProductService tourProductService;

    @Autowired
    private UserService userService;


    /**
     * 新增订单
     * @param userId
     * @param orderUserDetail
     * @param productId
     * @return
     */
    public Integer addOrder(Integer userId , OrderUserDetail orderUserDetail ,Integer productId){

        Integer tourOrderId  = transactionTemplate.execute(status -> {

            TourProduct tourProduct = tourProductService.getById(productId);

            //TODO 检查产品状态


            //添加订单
            TourOrder tourOrder = new TourOrder();

            tourOrder.setCreateTime(new Date());
            tourOrder.setUserId(userId);
            tourOrder.setTourProductId(productId);
            tourOrder.setState(Constants.NOT_PAY_STATE);

            Integer orderId = save(tourOrder);

            //订单详细
            TourOrderDetail tourOrderDetail = BeanUtil.copyBean(orderUserDetail, TourOrderDetail.class);
            tourOrderDetail.setOrderId(orderId);
            BigDecimal price = tourProduct.getPrice();
            tourOrderDetail.setPrice(price);

            tourOrderDetailService.save(tourOrderDetail);

            return orderId;
        });

        return tourOrderId;
    }


    /**
     * 通过产品ID获得对应的订单
     * @param tourProductId
     * @param startIndex
     * @param pageSize
     * @return
     */
    public List<AdminProductOrderItemResp> getByTourProductIdPage(Integer tourProductId , Integer startIndex , Integer pageSize){
        List<TourOrder> tourOrderList = getByProductId(tourProductId, startIndex, pageSize);

        List<AdminProductOrderItemResp> adminProductOrderItemRespList = new ArrayList<>();

        for(TourOrder tourOrder : tourOrderList){
            AdminProductOrderItemResp adminOrderItemResp = new AdminProductOrderItemResp();

            Integer userId = tourOrder.getUserId();

            User user = userService.getById(userId);
            String nickname = user.getNickname();
            adminOrderItemResp.setNickname(nickname);

            //订单状态
            Integer state = tourOrder.getState();
            adminOrderItemResp.setOrderState(state);

            Integer tourOrderId = tourOrder.getId();
            TourOrderDetail orderDetail = tourOrderDetailService.getByOrderId(tourOrderId);
            adminOrderItemResp.setOrderId(tourOrderId);

            //身份证
            String idCardNumber = orderDetail.getIdCardNumber();
            adminOrderItemResp.setIdCardNumber(idCardNumber);

            //真实姓名
            String realName = orderDetail.getRealName();
            adminOrderItemResp.setRealName(realName);

            //性别
            Integer gender = orderDetail.getGender();
            adminOrderItemResp.setGender(gender);

            //电话号码
            String phoneNumber = orderDetail.getPhoneNumber();
            adminOrderItemResp.setPhoneNumber(phoneNumber);

            //订单创建时间
            Date createTime = tourOrder.getCreateTime();
            adminOrderItemResp.setOrderCreateTime(createTime);

            adminProductOrderItemRespList.add(adminOrderItemResp);
        }

        return adminProductOrderItemRespList;
    }

    public Integer countByTourProductId(Integer tourProductId){
        return tourOrderMapper.countByTourProductId(tourProductId);
    }

    public TourOrder getById(Integer id){
        return tourOrderMapper.selectById(id);
    }

    private List<TourOrder> getByProductId(Integer tourProductId ,Integer startIndex ,Integer pageSize){
        return tourOrderMapper.selectByTourProductId(tourProductId, startIndex, pageSize);
    }

    private Integer save(TourOrder tourOrder){
        tourOrderMapper.insert(tourOrder);

        return tourOrder.getId();
    }

    /**
     * 付款成功
     * @param orderId
     */
    public void paySuccess(Integer orderId){
        updateByFromState(orderId, Constants.NOT_PAY_STATE, Constants.HAS_PAY_STATE);
    }

    private Integer updateByFromState(Integer orderId ,Integer fromState ,Integer toState){
        return tourOrderMapper.updateState(orderId ,fromState ,toState);
    }

    public BigDecimal getFeeByOrderId(Integer orderId){
        TourOrderDetail orderDetail = tourOrderDetailService.getByOrderId(orderId);

        BigDecimal price = orderDetail.getPrice();

        return price;
    }

    /**
     * 订单是否已经付款
     * @param tourOrderId
     * @return
     */
    public Boolean isOrderPaid(Integer tourOrderId){

        TourOrder tourOrder = getById(tourOrderId);

        if(tourOrder.getState().equals(Constants.HAS_PAY_STATE)){
            return true;
        }

        return false;
    }


    private static class Constants{
        private final static Integer NOT_PAY_STATE = 0;
        private final static Integer HAS_PAY_STATE = 1;
    }
}
