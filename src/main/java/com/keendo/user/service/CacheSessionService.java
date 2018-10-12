package com.keendo.user.service;

import com.keendo.user.model.SessionToken;
import com.keendo.user.service.utils.TimeUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheSessionService {

    private static Map<String, SessionToken> cache = new ConcurrentHashMap<>();

    private static class Constant {
        private static final Integer SESSION_VALID_TIME = 2;//session有效期2小时
    }

    public void setUserId(String token, Integer userId) {
        SessionToken sessionToken = new SessionToken();
        sessionToken.setUserId(userId);
        sessionToken.setCreateTime(new Date());

        cache.put(token, sessionToken);
    }

    public Integer getUserId(String token) {

        SessionToken sessionToken = cache.get(token);

        if (sessionToken == null) {
            return null;
        }

        Date createTime = sessionToken.getCreateTime();
        Date validEndTime = TimeUtils.hourOffset(createTime, Constant.SESSION_VALID_TIME);
        //失效
        if (validEndTime.compareTo(new Date()) <= 0) {
            cache.remove(token);
            return null;
        }

        Integer userId = sessionToken.getUserId();

        return userId;
    }

}
