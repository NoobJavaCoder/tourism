package com.keendo.biz.mapper;

import com.keendo.biz.model.AdminUser;

/**
 * Created by bint on 2018/10/8.
 */
public interface AdminUserMapper {

    AdminUser selectByUsername(String username);

    AdminUser selectById(Integer id);
}
