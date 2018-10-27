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

    int updateSelective(TourProduct tourProduct);

    int deleteById(Integer id);

    int updateStateById(@Param("state") Integer state, @Param("id") Integer id);

    int updateStateByIdAndFromState(@Param("fromState") Integer fromState, @Param("id") Integer id, @Param("toState") Integer state);

    /**
     * 未下架产品列表分页
     *
     * @param startIndex
     * @param pageSize
     * @return
     */
    List<TourProduct> selectByPageAndState(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("state") Integer state);

    /**
     * 查询未下架的产品总数
     *
     * @param state
     * @return
     */
    Integer countByState(Integer state);


    /**
     * 查询小于该状态值的旅游产品
     * @param state
     * @return
     */
    List<TourProduct> selectByLTState(Integer state);
}
