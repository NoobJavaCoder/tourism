package com.keendo.biz.controller.admin.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by bint on 2018/10/8.
 */
public class AdminLoginUtils {

    private final static String ADMIN_USER_ATTTR = "ADMIN_USER_ATTTR";

    /**
     * 登陆操作
     * @param admimUserId
     */
    public static void login(Integer admimUserId){

        HttpSession httpSession = getSession();

        httpSession.setAttribute(ADMIN_USER_ATTTR, admimUserId);
    }

    /**
     * 是否登陆
     * @return
     */
    public static Boolean isLogin(){
        HttpSession httpSession = getSession();

        Integer admimUserId = (Integer) httpSession.getAttribute(ADMIN_USER_ATTTR);

        if(admimUserId == null){
            return false;
        }

        return true;
    }

    /**
     * 登出操作
     */
    public static void logout(){
        HttpSession httpSession = getSession();

        httpSession.removeAttribute(ADMIN_USER_ATTTR);
    }

    public static Integer getAdminUserId(){
        HttpSession httpSession = getSession();

        Integer adminUserId = (Integer) httpSession.getAttribute(ADMIN_USER_ATTTR);

        return adminUserId;
    }

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
    }

    private static HttpSession getSession(){
        HttpSession session = getRequest().getSession();
        return session;
    }
}
