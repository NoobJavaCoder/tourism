package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.architecture.utils.Log;
import com.keendo.biz.mapper.TourOrderMapper;
import com.keendo.biz.model.*;
import com.keendo.biz.service.bean.order.AdminProductOrderItemResp;
import com.keendo.biz.service.bean.order.MyOrderDetail;
import com.keendo.biz.service.bean.order.MyOrderItem;
import com.keendo.biz.service.bean.order.OrderUserDetail;
import com.keendo.biz.service.utils.RandomUtil;
import com.keendo.user.model.User;
import com.keendo.user.service.UserService;
import com.keendo.user.service.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

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

    @Autowired
    private OrderOptService orderOptService;

    @Autowired
    private UserIdempotentService userIdempotentService;

    @Autowired
    private UserInfoService userInfoService;


    //订单保留时间，超过则取消
    private final static Long ORDER_RETENTION_TIME = 15 * 60 * 1000L;

    /**
     * 得到我的订单详细
     *
     * @param tourOrderId
     * @return
     */
    public MyOrderDetail getMyOrderDetail(Integer tourOrderId) {

        MyOrderDetail myOrderDetail = new MyOrderDetail();
        myOrderDetail.setOrderId(tourOrderId);

        TourOrder tourOrder = getById(tourOrderId);

        Integer tourProductId = tourOrder.getTourProductId();
        //产品ID
        myOrderDetail.setTourProductId(tourProductId);

        TourProduct tourProduct = tourProductService.getById(tourProductId);

        //产品封面图片
        String coverImgUrl = tourProduct.getCoverImgUrl();
        myOrderDetail.setCoverImgUrl(coverImgUrl);

        //产品名字
        String title = tourProduct.getTitle();
        myOrderDetail.setProductTitle(title);

        //出发时间
        Date departureTime = tourProduct.getDepartureTime();
        myOrderDetail.setDepartureTime(departureTime);

        //花费
        TourOrderDetail orderDetail1 = tourOrderDetailService.getByOrderId(tourOrderId);
        BigDecimal price = orderDetail1.getPrice();
        myOrderDetail.setPrice(price);


        //订单轨迹
        List<OrderOpt> orderOptList = orderOptService.getListByOrderId(tourOrderId);
        for (OrderOpt orderOpt : orderOptList) {
            Integer toState = orderOpt.getToState();
            if (toState.equals(Constants.NOT_PAY_STATE)) {
                myOrderDetail.setOrderCreateTime(orderOpt.getCreateTime());
            }

            if (toState.equals(Constants.HAS_PAY_STATE)) {
                myOrderDetail.setOrderPayTime(orderOpt.getCreateTime());
            }
        }

        //订单人信息
        TourOrderDetail orderDetail = orderDetail1;

        //电话号码
        String phoneNumber = orderDetail.getPhoneNumber();
        myOrderDetail.setPhoneNumber(phoneNumber);


        //真实名字
        String realName = orderDetail.getRealName();
        myOrderDetail.setRealName(realName);

        //身份证号码
        String idCardNumber = orderDetail.getIdCardNumber();
        myOrderDetail.setIdCardNumber(idCardNumber);

        return myOrderDetail;
    }

    /**
     * 新增订单
     *
     * @param userId
     * @param productId
     * @return
     */
    public Integer addOrder(Integer userId, Integer productId) {
        UserInfo userInfo = userInfoService.getByUserId(userId);

        if (userInfo == null) {
            throw new BizException("缺乏用户资料");
        }

        OrderUserDetail orderUserDetail = new OrderUserDetail();

        String phoneNo = userInfo.getPhoneNo();
        orderUserDetail.setPhoneNumber(phoneNo);

        String idCardNo = userInfo.getIdCardNo();
        orderUserDetail.setIdCardNumber(idCardNo);

        Integer sex = userInfo.getSex();
        orderUserDetail.setGender(sex);

        String realName = userInfo.getRealName();
        orderUserDetail.setRealName(realName);

        return addOrder(userId, orderUserDetail, productId);
    }

    /**
     * 生成唯一的系统订单号
     * 时间戳 + 随机数 = 32位字符串
     *
     * @return
     */
    private String createOrderSn() {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        Integer remainLen = Constants.ORDER_SN_LENGTH - timeStamp.length();
        String radomNumber = RandomUtil.getRandomString(remainLen);
        return timeStamp + radomNumber;
    }


    /**
     * 新增订单
     *
     * @param userId
     * @param orderUserDetail
     * @param productId
     * @return
     */
    public Integer addOrder(Integer userId, OrderUserDetail orderUserDetail, Integer productId) {

        Integer idempotentId = userIdempotentService.add(userId);

        Integer tourOrderId = null;
        try {
            tourOrderId = transactionTemplate.execute(status -> {

                TourProduct tourProduct = tourProductService.getById(productId);

                Integer state = tourProduct.getState();
                if (!state.equals(TourProductService.Constants.ON_GOING_STATE)) {
                    throw new BizException("该产品状态无法下单");
                }

                //检查自己是否已经下单
                Boolean hasOrder = isHasOrder(productId, userId);

                if (hasOrder) {
                    throw new BizException("已经存在订单");
                }

                //添加订单
                TourOrder tourOrder = new TourOrder();

                tourOrder.setCreateTime(new Date());
                tourOrder.setUserId(userId);
                tourOrder.setTourProductId(productId);
                tourOrder.setState(Constants.NOT_PAY_STATE);

                String orderSn = this.createOrderSn();
                tourOrder.setOrderSn(orderSn);

                Integer orderId = save(tourOrder);

                //订单详细
                TourOrderDetail tourOrderDetail = BeanUtil.copyBean(orderUserDetail, TourOrderDetail.class);
                tourOrderDetail.setOrderId(orderId);
                BigDecimal price = tourProduct.getPrice();
                tourOrderDetail.setPrice(price);
                tourOrderDetail.setOrderSn(orderSn);

                tourOrderDetailService.save(tourOrderDetail);

                //订单轨迹
                orderOptService.add(orderId, null, Constants.NOT_PAY_STATE, userId);

                //检查是否满员
                Integer hasPaidCount = countHasPaidByTourProductId(productId);
                Integer unPaidCount = countUnPaidByTourProductId(productId);

                Integer orderCount = hasPaidCount + unPaidCount;
                Integer maxParticipantNum = tourProduct.getMaxParticipantNum();

                //如果满员则修改产品状态
                if (maxParticipantNum.equals(orderCount)) {
                    Integer fullStateResult = tourProductService.fullState(productId);

                    if (fullStateResult.equals(0)) {
                        throw new BizException("产品状态有误");
                    }
                }

                return orderId;
            });
        } catch (Exception e) {
            Log.e(e);
            throw e;
        } finally {
            userIdempotentService.unlock(idempotentId);
        }

        return tourOrderId;
    }


    /**
     * 通过产品ID获得对应的订单
     *
     * @param tourProductId
     * @param startIndex
     * @param pageSize
     * @return
     */
    public List<AdminProductOrderItemResp> getByTourProductIdPage(Integer tourProductId, Integer startIndex, Integer pageSize) {
        List<TourOrder> tourOrderList = getByProductId(tourProductId, startIndex, pageSize);

        List<AdminProductOrderItemResp> adminProductOrderItemRespList = new ArrayList<>();

        for (TourOrder tourOrder : tourOrderList) {
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

    public Integer countByTourProductId(Integer tourProductId) {
        return tourOrderMapper.countByTourProductId(tourProductId);
    }

    public Integer countUnPaidByTourProductId(Integer tourProductId) {
        return tourOrderMapper.countByTourProductIdAndState(tourProductId, Constants.NOT_PAY_STATE);
    }

    public Integer countHasPaidByTourProductId(Integer tourProductId) {
        return tourOrderMapper.countByTourProductIdAndState(tourProductId, Constants.HAS_PAY_STATE);
    }

    public TourOrder getById(Integer id) {
        return tourOrderMapper.selectById(id);
    }

    /**
     * 根据系统唯一订单号获取订单对象
     *
     * @param orderSn
     * @return
     */
    public TourOrder getByOrderSn(String orderSn) {
        return tourOrderMapper.selectByOrderSn(orderSn);
    }

    private List<TourOrder> getByProductId(Integer tourProductId, Integer startIndex, Integer pageSize) {
        return tourOrderMapper.selectByTourProductId(tourProductId, startIndex, pageSize);
    }

    private Integer save(TourOrder tourOrder) {
        tourOrderMapper.insert(tourOrder);

        return tourOrder.getId();
    }

    /**
     * 付款成功
     *
     * @param orderSn:系统唯一订单号
     */
    public void paySuccess(String orderSn) {

        transactionTemplate.execute(status -> {

            TourOrder tourOrder = getByOrderSn(orderSn);

            Integer orderId = tourOrder.getId();

            updateByFromState(orderId, Constants.NOT_PAY_STATE, Constants.HAS_PAY_STATE);

            Integer userId = tourOrder.getUserId();

            //订单轨迹
            orderOptService.add(orderId, Constants.NOT_PAY_STATE, Constants.HAS_PAY_STATE, userId);

            return null;
        });
    }


    private Boolean isHasOrder(Integer productId, Integer userId) {
        List<TourOrder> tourOrderList = getByProductIdAndUserId(productId, userId);

        for (TourOrder tourOrder : tourOrderList) {
            Integer state = tourOrder.getState();
            if (state.equals(Constants.NOT_PAY_STATE)) {
                return true;
            }

            if (state.equals(Constants.HAS_PAY_STATE)) {
                return true;
            }
        }

        return false;
    }

    private Integer updateByFromState(Integer orderId, Integer fromState, Integer toState) {
        return tourOrderMapper.updateState(orderId, fromState, toState);
    }


    /**
     * 取消订单
     *
     * @param orderId
     */
    public void cancelOrder(Integer orderId, Integer userId) {

        TourOrder tourOrder = getById(orderId);
        if (tourOrder == null) {
            throw new BizException("找不到该订单");
        }

        Integer tourOrderUserId = tourOrder.getUserId();

        if (!tourOrderUserId.equals(userId)) {
            throw new BizException("用户有误");
        }

        if (!tourOrder.getState().equals(Constants.NOT_PAY_STATE)) {
            throw new BizException("未付款的订单才能取消");
        }

        transactionTemplate.execute(status -> {

            Integer updateResult = tourOrderMapper.updateState(orderId, Constants.NOT_PAY_STATE, Constants.CANCEL_STATE);
            if (updateResult == 0) {
                throw new BizException("取消订单失败");
            }

            return updateResult;
        });

    }

    /**
     * 得到订单的花费
     *
     * @param orderSn:系统订单号
     * @return
     */
    public BigDecimal getFeeByOrderId(String orderSn) {
        TourOrderDetail orderDetail = tourOrderDetailService.getByOrderSn(orderSn);

        BigDecimal price = orderDetail.getPrice();

        return price;
    }

    /**
     * 订单是否已经付款
     *
     * @param orderSn:系统订单号
     * @return
     */
    public Boolean isOrderPaid(String orderSn) {

        TourOrder tourOrder = getByOrderSn(orderSn);

        if (tourOrder.getState().equals(Constants.HAS_PAY_STATE)) {
            return true;
        }

        return false;
    }

    /**
     * 取消未付款的订单
     */
    public void cancelUnPaidTourOrder() {
        Long currentTimeMillis = System.currentTimeMillis();
        Long time = currentTimeMillis - ORDER_RETENTION_TIME;

        Date date = new Date(time);
        updateByStateAndCreateTime(Constants.NOT_PAY_STATE, Constants.CANCEL_STATE, date);
    }

    private Integer updateByStateAndCreateTime(Integer fromState, Integer toState, Date createTime) {
        Log.i("cancel tourOrder that is unPaid.");

        List<TourOrder> tourOrderList = getByStateAndCreateTime(fromState, createTime);

        for (TourOrder tourOrder : tourOrderList) {

            transactionTemplate.execute(status -> {
                Integer id = tourOrder.getId();
                updateByFromState(id, fromState, toState);

                orderOptService.addSystemOpt(id, fromState, toState);

                return null;
            });

        }

        if (tourOrderList == null) {
            return 0;
        } else {
            return tourOrderList.size();
        }
    }

    public List<TourOrder> getByStateAndCreateTime(Integer state, Date createTime) {

        return tourOrderMapper.selectByStateAndCreateTime(state, createTime);
    }

    private List<TourOrder> getByProductIdAndUserId(Integer tourOrder, Integer userId) {
        return tourOrderMapper.selectByProductIdAndUserId(tourOrder, userId);
    }

    /**
     * 根据旅游产品id查询已经下订单的用户的id列表,limit 3人
     *
     * @param tourProductId:旅游产品id
     * @return
     */
    public List<Integer> getOrderedUserIdList(Integer tourProductId) {
        return tourOrderMapper.selectOrderedUserIdList(tourProductId, Constants.NOT_PAY_STATE, Constants.HAS_PAY_STATE);
    }


    /**
     * 通过用户id获取分页
     *
     * @param userId
     * @param startIndex
     * @param pageSize
     * @return
     */
    private List<TourOrder> getPageByUserId(Integer userId, Integer startIndex, Integer pageSize) {
        return tourOrderMapper.selectPageBydAndUserId(userId, startIndex, pageSize);
    }

    /**
     * 获取我的订单列表
     *
     * @param userId
     * @param startIndex
     * @param pageSize
     * @return
     */
    public List<MyOrderItem> getMyOrderList(Integer userId, Integer startIndex, Integer pageSize) {

        List<MyOrderItem> myOrderItemList = new ArrayList<>();

        List<TourOrder> tourOrderList = getPageByUserId(userId, startIndex, pageSize);

        for (TourOrder tourOrder : tourOrderList) {
            MyOrderItem myOrderItem = new MyOrderItem();

            Integer orderId = tourOrder.getId();
            myOrderItem.setOrderId(orderId);

            TourOrderDetail tourOrderDetail = tourOrderDetailService.getByOrderId(orderId);

            //花费
            BigDecimal price = tourOrderDetail.getPrice();
            myOrderItem.setPrice(price);

            //产品出发时间
            Integer tourProductId = tourOrder.getTourProductId();
            TourProduct tourProduct = tourProductService.getById(tourProductId);
            myOrderItem.setDepartureTime(tourProduct.getDepartureTime());

            //产品标题
            myOrderItem.setProductTitle(tourProduct.getTitle());

            //产品封面
            myOrderItem.setCoverImgUrl(tourProduct.getCoverImgUrl());

            //订单状态
            Integer state = tourOrder.getState();
            myOrderItem.setTourOrderState(state);

            myOrderItemList.add(myOrderItem);

        }

        return myOrderItemList;
    }

    private static class Constants {
        private final static Integer NOT_PAY_STATE = 0;//未付款
        private final static Integer HAS_PAY_STATE = 1;//已经付款
        private final static Integer CANCEL_STATE = 2;//取消
        private final static Integer HAS_RETURN_STATE = 3;//已经退款

        private final static Integer ORDER_SN_LENGTH = 32;//系统订单号位数
    }
}
