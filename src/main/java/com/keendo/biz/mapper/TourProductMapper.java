package com.keendo.biz.mapper;

import com.keendo.biz.model.TourProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TourProductMapper {

    int insert(TourProduct record);

    TourProduct selectById(Integer id);

    List<TourProduct> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    Integer count();

    int updateByPrimaryKey(TourProduct record);

    int deleteById(Integer id);

    List<TourProduct> selectByState(Integer state);

    int updateStateById(@Param("state") Integer state, @Param("id") Integer id);

    int updateStateByIdAndFromState(@Param("fromState") Integer fromState, @Param("id") Integer id ,@Param("toState") Integer state);

}
