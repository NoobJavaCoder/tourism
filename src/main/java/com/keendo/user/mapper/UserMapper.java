package com.keendo.user.mapper;


import com.keendo.user.model.User;

/**
 * Created by bint on 2018/9/10.
 */
public interface UserMapper {
    User selectById(Integer id);

    Integer insert(User record);

    Integer update(User record);

    Integer countByOpenId(String openId);

    User selectByOpenId(String openId);
}
