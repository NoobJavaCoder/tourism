package com.keendo.biz.controller.admin;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.controller.admin.bean.adminuser.AdminUserLoginVO;
import com.keendo.biz.controller.admin.utils.AdminLoginUtils;
import com.keendo.biz.model.AdminUser;
import com.keendo.biz.service.AdminUserService;
import com.keendo.biz.service.bean.adminuser.LoginAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bint on 2018/10/8.
 */
@RestController
@RequestMapping(value = "/admin/admin-user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RespBase login(@RequestBody AdminUserLoginVO adminUserLoginVO) {

        String password = adminUserLoginVO.getPassword();
        String username = adminUserLoginVO.getUsername();

        Boolean able = adminUserService.isAbleLogin(username, password);

        if (able){
            AdminUser adminUser = adminUserService.getByAdminUser(username);
            AdminLoginUtils.login(adminUser.getId());
        }

        return RespHelper.ok();
    }


    @RequestMapping(value = "/isLogin", method = RequestMethod.POST)
    public RespBase isLogin() {

        Boolean result = AdminLoginUtils.isLogin();

        return RespHelper.ok(result);
    }


    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public RespBase logout() {

        AdminLoginUtils.logout();

        return RespHelper.ok();
    }

    @RequestMapping(value = "/login-user/info", method = RequestMethod.POST)
    public RespBase getLoginUserInfo() {

        Integer adminUserId = AdminLoginUtils.getAdminUserId();

        if(adminUserId == null){
            return RespHelper.nologin();
        }

        LoginAdminUser loginAdminUser = adminUserService.getLoginAdminUser(adminUserId);

        return RespHelper.ok(loginAdminUser);
    }
}
