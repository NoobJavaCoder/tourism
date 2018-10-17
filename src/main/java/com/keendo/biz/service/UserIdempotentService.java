package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.biz.mapper.UserIdempotentMapper;
import com.keendo.biz.model.UserIdempotent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by bint on 2018/8/13.
 */
@Service
public class UserIdempotentService {

    @Autowired
    private UserIdempotentMapper userIdempotentMapper;

    public Integer add(Integer userId) throws BizException {
        UserIdempotent userIdempotent = new UserIdempotent();
        userIdempotent.setUserId(userId);

        userIdempotent.setState(Contanst.LOCK_STATE);

        userIdempotent.setCreateTime(new Date());

        Integer result = userIdempotentMapper.insert(userIdempotent);

        if (result.intValue() == 0) {
            throw new BizException("请勿重复提交");
        }

        return userIdempotent.getId();
    }

    public void unlock(Integer id) {
        this.updateState(id, Contanst.UNLOCK_STATE);
    }

    private Integer updateState(Integer id, Integer state) {
        return userIdempotentMapper.updateState(id, state);
    }


    private static class Contanst {
        private final static Integer LOCK_STATE = 1;
        private final static Integer UNLOCK_STATE = 2;
    }



}
