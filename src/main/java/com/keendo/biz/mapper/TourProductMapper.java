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
}
