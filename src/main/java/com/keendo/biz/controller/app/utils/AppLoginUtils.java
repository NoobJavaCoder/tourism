package com.keendo.biz.controller.app.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by bint on 2018/10/8.
 */
public class AppLoginUtils {

    private final static String APP_USER_ATTTR = "APP_USER_ATTTR";

    /**
     * 登陆操作
     * @param admimUserId
     */
    public static void login(Integer admimUserId){

        HttpSession httpSession = getSession();

        httpSession.setAttribute(APP_USER_ATTTR, admimUserId);
    }


    public static Integer getUserId(){
        HttpSession httpSession = getSession();

        Integer appUserId = (Integer) httpSession.getAttribute(APP_USER_ATTTR);

        return appUserId;
    }

    /**
     * 是否登陆
     * @return
     */
    public static Boolean isLogin(){

        Integer appUserId = getUserId();

        if(appUserId == null){
            return false;
        }

        return true;
    }

    /**
     * 登出操作
     */
    public static void logout(){
        HttpSession httpSession = getSession();

        httpSession.removeAttribute(APP_USER_ATTTR);
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
