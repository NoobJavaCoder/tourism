package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.architecture.utils.Log;
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

    //订单保留时间，超过则取消
    private final static Long ORDER_RETENTION_TIME = 15 * 60 * 1000L;

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

            Integer state = tourProduct.getState();
            if(!state.equals(TourProductService.Constants.ON_GOING_STATE)){
                throw new BizException("该产品状态无法下单");
            }

            //TODO 检查自己是否已经下单

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

            //检查是否满员
            Integer hasPaidCount = countHasPaidByTourProductId(productId);
            Integer unPaidCount = countUnPaidByTourProductId(productId);

            Integer orderCount = hasPaidCount + unPaidCount;
            Integer maxParticipantNum = tourProduct.getMaxParticipantNum();
            //如果满员则修改产品状态
            if(maxParticipantNum.equals(orderCount)){
                Integer fullStateResult = tourProductService.fullState(productId);

                if(fullStateResult.equals(0)){
                    throw new BizException("产品状态有误");
                }
            }

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

    public Integer countUnPaidByTourProductId(Integer tourProductId){
        return tourOrderMapper.countByTourProductIdAndState(tourProductId,Constants.NOT_PAY_STATE);
    }

    public Integer countHasPaidByTourProductId(Integer tourProductId){
        return tourOrderMapper.countByTourProductIdAndState(tourProductId,Constants.HAS_PAY_STATE);
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


    /**
     * 取消订单
     * @param orderId
     */
    public void cancelOrder(Integer orderId ,Integer userId){

        TourOrder tourOrder = getById(orderId);
        if(tourOrder == null){
            throw new BizException("找不到该订单");
        }

        Integer tourOrderUserId = tourOrder.getUserId();

        if(!tourOrderUserId.equals(userId)){
            throw new BizException("用户有误");
        }

        if (!tourOrder.getState().equals(Constants.NOT_PAY_STATE)) {
            throw new BizException("未付款的订单才能取消");
        }

        Integer updateResult = tourOrderMapper.updateState(orderId, Constants.NOT_PAY_STATE, Constants.CANCEL_STATE);
        if(updateResult == 0){
            throw new BizException("取消订单失败");
        }
    }

    /**
     * 得到订单的花费
     * @param orderId
     * @return
     */
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

    /**
     * 取消未付款的订单
     */
    public void cancelUnPaidTourOrder(){
        Long currentTimeMillis = System.currentTimeMillis();
        Long time = currentTimeMillis - ORDER_RETENTION_TIME;

        Date date = new Date(time);
        updateByStateAndCreateTime(Constants.NOT_PAY_STATE, Constants.CANCEL_STATE, date);
    }

    public Integer updateByStateAndCreateTime(Integer fromState ,Integer toState ,Date createTime){
        Log.i("cancel tourOrder that is unPaid.");
        return tourOrderMapper.updateByStateAndCreateTime(fromState, toState, createTime);
    }


    private static class Constants{
        private final static Integer NOT_PAY_STATE = 0;
        private final static Integer HAS_PAY_STATE = 1;
        private final static Integer CANCEL_STATE = 2;
    }
}
