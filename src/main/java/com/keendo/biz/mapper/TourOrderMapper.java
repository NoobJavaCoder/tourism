package com.keendo.biz.mapper;

import com.keendo.biz.model.TourOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by bint on 2018/10/10.
 */
public interface TourOrderMapper {
    int insert(TourOrder tourOrder);

    List<TourOrder> selectByTourProductId(@Param("tourProductId") Integer tourProductId ,@Param("startIndex") Integer startIndex ,@Param("pageSize") Integer pageSize);

    int updateState(@Param("orderId") Integer orderId ,@Param("fromState") Integer fromState ,@Param("toState") Integer toState);

    TourOrder selectById(Integer id);

    int countByTourProductId(Integer tourProductId);
}
