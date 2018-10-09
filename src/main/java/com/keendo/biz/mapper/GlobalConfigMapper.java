package com.keendo.biz.mapper;


import com.keendo.biz.model.GlobalConfig;

import java.util.List;

/**
 * 全局配置表接口
 *
 * @author hebo2
 */
public interface GlobalConfigMapper {

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<GlobalConfig> selectAll();

    int deleteByPrimaryKey(String key);

    int insert(GlobalConfig record);

    int insertSelective(GlobalConfig record);

    GlobalConfig selectByPrimaryKey(String key);

    int updateByPrimaryKeySelective(GlobalConfig record);

    int updateByPrimaryKey(GlobalConfig record);

    /**
     * 用于测试数据库连接，结果返回1
     * insert into g_global_config (`KEY`,`VALUE`,`DESC`) values ('Z_FOR_TEST','testconnect','用于测试数据库连接');
     *
     * @return
     */
    public String selectTest();

}
