package com.keendo.biz.service;

import com.keendo.architecture.exception.BizException;
import com.keendo.biz.mapper.UserInfoMapper;
import com.keendo.biz.model.UserInfo;
import com.keendo.biz.service.bean.userinfo.AdminUserInfo;
import com.keendo.biz.service.utils.BeanUtils;
import com.keendo.user.model.User;
import com.keendo.user.service.UserService;
import com.keendo.user.service.utils.VerifyUtil;
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
     * @param startIndex
     * @param pageSize
     * @return
     */
    public List<AdminUserInfo>  getAdminUserInfoList(Integer startIndex,Integer pageSize){
        List<UserInfo> userInfos = userInfoMapper.selectByPage(startIndex, pageSize);

        List<AdminUserInfo> list = new ArrayList<>();

        for(UserInfo userInfo:userInfos){
            AdminUserInfo adminUserInfo = BeanUtils.copyBean(userInfo, AdminUserInfo.class);
            list.add(adminUserInfo);
        }

        return list;
    }

    /**
     * 小程序展示用户信息
     * @param userId:用户id
     * @return
     */
    public UserInfo getAppUserInfo(Integer userId){
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if(userInfo == null){
            throw new BizException("用户信息不存在");
        }
        return userInfo;
    }

    public UserInfo getUserInfoByRealNameKeyWord(String keyword){
        return userInfoMapper.selectByRealName(keyword);
    }

    /**
     * 小程序"我的资料"保存用户信息
     * @param userInfo
     */
    public void saveUserInfo(UserInfo userInfo){
        //TODO:校验手机号,身份证号,
        String realName = userInfo.getRealName();
        if(!VerifyUtil.isRealName(realName)){
            throw new BizException("请填写真实姓名");
        }

        String phoneNo = userInfo.getPhoneNo();
        if(!VerifyUtil.isPhone(phoneNo)){

        }

        Integer userId = userInfo.getUserId();
        User user = userService.getById(userId);
        if(user == null){
            throw new BizException("用户不存在");
        }

        String nickname = user.getNickname();
        userInfo.setNickname(nickname);

        this.save(userInfo);
    }

    private Integer save(UserInfo userInfo){
        return userInfoMapper.insert(userInfo);
    }

}
