package com.keendo.user.controlller;

import com.keendo.user.service.CacheSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Debate Controller基类
 */
public class BaseController {

    public static final String HEADER_TOKEN_STR = "token";

    @Autowired
    private CacheSessionService cacheSessionService;


    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
    }

    protected Integer getUserId() {
        String token = getRequest().getHeader(HEADER_TOKEN_STR);

        //有些接口不需要登陆即可访问,不会携带token,传递null值,导致map.get空指针
        if (token == null) {
            return null;
        }

        Integer userId = cacheSessionService.getUserId(token);

        return userId;
    }



}
