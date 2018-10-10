package com.keendo.biz.mapper;

import com.keendo.biz.model.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {

    int insert(UserInfo record);

    UserInfo selectById(Integer id);

    UserInfo selectByUserId(Integer userId);

    UserInfo selectByRealName(String keyword);

    List<UserInfo> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

    Integer count();

    int updateByPrimaryKey(UserInfo record);

    int deleteById(Integer id);
}
