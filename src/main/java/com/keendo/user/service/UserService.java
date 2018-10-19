package com.keendo.user.service;

import com.keendo.user.mapper.UserMapper;
import com.keendo.user.model.User;
import com.keendo.user.service.bean.user.LoginRet;
import com.keendo.user.service.bean.user.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bint on 2018/9/10.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 根据id获取用户信息
     *
     * @param id:用户id
     * @return
     */
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * 根据openId获取用户信息
     *
     * @param openId
     * @return
     */
    public User getByOpenId(String openId) {
        return userMapper.selectByOpenId(openId);
    }

    /**
     * 根据用户openid判断是否存在
     *
     * @param openId
     * @return
     */
    public boolean isExist(String openId) {
        Integer count = userMapper.countByOpenId(openId);
        return count > 0;
    }

    public Integer save(User user) {
        userMapper.insert(user);
        return user.getId();
    }

    public void update(User user) {
        userMapper.update(user);
    }

    /**
     * 根据用户id获取"我的"用户信息vo
     * @param userId:用户id
     * @return
     */
    public UserInfoVO getUserInfoVO(Integer userId){
        User user = this.getById(userId);

        UserInfoVO vo = new UserInfoVO();

        String headImgUrl = user.getHeadImgUrl();
        vo.setHeadImgUrl(headImgUrl);

        String nickname = user.getNickname();
        vo.setNickname(nickname);

        return vo;
    }

    /**
     * 检查登录状态
     *
     * @param userId
     * @return
     */
    public LoginRet isLogin(Integer userId) {
        LoginRet loginRet = new LoginRet();
        loginRet.setLogin(Constant.NOT_LOGIN);

        if (userId != null) {
            loginRet.setLogin(Constant.IS_LOGIN);
        }

        return loginRet;
    }


    private static class Constant {
        private static final Integer NOT_LOGIN = 0;//未登陆
        private static final Integer IS_LOGIN = 1;//已登陆
    }


    public static void main(String[] args) {
        System.out.println(args.length);
        for (int i=0;i<args.length;i++){
            System.out.println(args[0]);
        }
    }
}
