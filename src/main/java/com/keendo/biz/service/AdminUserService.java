package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.biz.mapper.AdminUserMapper;
import com.keendo.biz.service.bean.adminuser.LoginAdminUser;

import com.keendo.biz.model.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bint on 2018/10/8.
 */
@Service
public class AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;


    public Boolean isAbleLogin(String username ,String password){

        if(username == null){
            throw new BizException("用户账号不能为空");
        }

        if(password == null){
            throw new BizException("用户密码不能为空");
        }

        AdminUser adminUser = getByAdminUser(username);

        if(adminUser == null){
            throw new BizException("账号有误");
        }

        String passwordFromDB = adminUser.getPassword();
        if(!passwordFromDB.equals(password)){
            throw new BizException("密码错误");
        }

        return true;
    }

    public AdminUser getByAdminUser(String username){
        return adminUserMapper.selectByUsername(username);
    }

    /**
     * 通过id获取
     * @param adminUserId
     * @return
     */
    public AdminUser getById(Integer adminUserId){
        AdminUser adminUser = adminUserMapper.selectById(adminUserId);
        return adminUser;
    }

    public LoginAdminUser getLoginAdminUser(Integer adminUserId){
        AdminUser adminUser = getById(adminUserId);

        LoginAdminUser loginAdminUser = new LoginAdminUser();
        loginAdminUser.setUsername(adminUser.getUsername());
        loginAdminUser.setAdminUserId(adminUserId);
        return loginAdminUser;

    }
}
