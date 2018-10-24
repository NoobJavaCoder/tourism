package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.biz.mapper.UserInfoMapper;
import com.keendo.biz.model.UserInfo;
import com.keendo.biz.service.bean.userinfo.AdminUserInfo;
import com.keendo.biz.service.bean.userinfo.UserInfoResp;
import com.keendo.biz.service.utils.BeanUtils;
import com.keendo.user.model.User;
import com.keendo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserService userService;

    /**
     * admin后台获取用户信息列表
     *
     * @param startIndex
     * @param pageSize
     * @return
     */
    public List<AdminUserInfo> getAdminUserInfoList(Integer startIndex, Integer pageSize) {
        List<UserInfo> userInfos = userInfoMapper.selectByPage(startIndex, pageSize);

        List<AdminUserInfo> list = new ArrayList<>();

        for (UserInfo userInfo : userInfos) {
            AdminUserInfo adminUserInfo = BeanUtils.copyBean(userInfo, AdminUserInfo.class);
            list.add(adminUserInfo);
        }

        return list;
    }

    /**
     * 小程序展示用户信息
     *
     * @param userId:用户id
     * @return
     */
    public UserInfoResp getAppUserInfo(Integer userId) {
        UserInfo userInfo = this.getByUserId(userId);

        if(userInfo == null){
            return null;
        }
        UserInfoResp userInfoResp = BeanUtils.copyBean(userInfo, UserInfoResp.class);
        return userInfoResp;
    }

    public UserInfo getUserInfoByRealNameKeyWord(String keyword) {
        return userInfoMapper.selectByRealName(keyword);
    }

    /**
     * 小程序"我的资料"保存用户信息
     *
     * @param userInfo:保存用户信息参数
     */
    public void saveUserInfo(UserInfo userInfo) {

        Integer userId = userInfo.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        UserInfo info = this.getByUserId(userId);
        if (info == null) {

            String nickname = user.getNickname();
            userInfo.setNickname(nickname);
            this.save(userInfo);
        } else {
            BeanUtils.copyPropertiesIgnoreNull(userInfo,info);
            this.update(info);
        }


    }

    public Integer update(UserInfo userInfo) {
        return userInfoMapper.updateByPrimaryKey(userInfo);
    }

    public Integer save(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

    public UserInfo getByUserId(Integer userId) {
        return userInfoMapper.selectByUserId(userId);
    }
}
