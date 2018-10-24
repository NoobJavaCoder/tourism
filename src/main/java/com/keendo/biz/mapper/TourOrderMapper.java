package com.keendo.biz.mapper;

import com.keendo.biz.model.TourOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by bint on 2018/10/10.
 */
public interface TourOrderMapper {
    int insert(TourOrder tourOrder);

    List<TourOrder> selectByTourProductId(@Param("tourProductId") Integer tourProductId, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    int updateState(@Param("orderId") Integer orderId, @Param("fromState") Integer fromState, @Param("toState") Integer toState);

    TourOrder selectById(Integer id);

    TourOrder selectByOrderSn(String orderSn);

    int countByTourProductId(Integer tourProductId);

    List<TourOrder> selectByStateAndCreateTime(@Param("state") Integer state, @Param("createTime") Date createTime);

    List<TourOrder> selectByProductIdAndUserId(@Param("productId") Integer productId, @Param("userId") Integer userId);

    List<TourOrder> selectPageBydAndUserId(@Param("userId") Integer userId, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    /**
     * 查询下了订单的用户id集合
     *
     * @param tourProductId:旅游产品id
     * @param state1:下单未付款
     * @param state2:已付款
     * @return
     */
    List<Integer> selectOrderedUserIdList(@Param("tourProductId") Integer tourProductId, @Param("state1") Integer state1, @Param("state2") Integer state2);


    /**
     * 根据旅游产品id和1种或多种状态值查询订单数量
     * @param tourProductId
     * @param states
     * @return
     */
    Integer countByTourProductIdAndStates(@Param("tourProductId") Integer tourProductId, @Param("states") List<Integer> states);
}
