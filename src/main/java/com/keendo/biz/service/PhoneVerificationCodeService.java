package com.keendo.biz.service;

import com.keendo.biz.mapper.PhoneVerificationCodeMapper;
import com.keendo.biz.model.PhoneVerificationCode;
import com.keendo.biz.service.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by bint on 2018/10/15.
 */
@Service
public class PhoneVerificationCodeService {

    @Autowired
    private CfgService cfgService;

    @Autowired
    private COSSmsService cosSmsService;

    @Autowired
    private PhoneVerificationCodeMapper phoneVerificationCodeMapper;

    private final static Integer VERIFICATION_CODE_LENGTH = 6;

    /**
     * 新增我的验证码
     * @param phoneNumber
     */
    public void addMyInofVerificationCode(String phoneNumber){

        String verificationCode = RandomUtil.radomNumber(VERIFICATION_CODE_LENGTH);

        //添加验证码
        this.add(verificationCode, phoneNumber ,Constants.MY_INFO_PHONE_TYPE);

        Integer templateId = cfgService.getInteger(CfgService.Constants.SMS_TPL_ID_USER_INFO);

        cosSmsService.sendMsg(phoneNumber, templateId, verificationCode);
    }

    private PhoneVerificationCode getByPhoneNumberAndType(String phoneNumber ,Integer type){
        return phoneVerificationCodeMapper.selectNewestByPhoneNumberAndType(phoneNumber, type);
    }


    private Integer add(String verificationCode , String phoneNumber ,Integer type){

        PhoneVerificationCode phoneVerificationCode = new PhoneVerificationCode();

        phoneVerificationCode.setPhoneNumber(phoneNumber);
        phoneVerificationCode.setCreateTime(new Date());
        phoneVerificationCode.setState(Constants.INACTIVATED_STATE);
        phoneVerificationCode.setVerificationCode(verificationCode);
        phoneVerificationCode.setType(type);

        return phoneVerificationCodeMapper.insert(phoneVerificationCode);
    }

    /**
     * 验证有效性
     * @param phoneNumber
     * @param verificationCode
     * @param type
     * @return
     */
    public Boolean verifyUsable(String phoneNumber ,String verificationCode ,Integer type){

        PhoneVerificationCode phoneVerificationCode = getByPhoneNumberAndType(phoneNumber ,type);

        if(phoneVerificationCode == null){
            return false;
        }

        //校验状态
        Integer state = phoneVerificationCode.getState();
        if(!Constants.INACTIVATED_STATE.equals(state)){
            return false;
        }

        //校验验证码是否相等
        String code = phoneVerificationCode.getVerificationCode();
        if(!code.equals(verificationCode)){
            return false;
        }

        //检验是否过期
        Date createTime = phoneVerificationCode.getCreateTime();
        Long timestamp = createTime.getTime();

        Date date = new Date();
        Long currentTime = date.getTime();
        if(currentTime - timestamp > 5 * 60 * 1000){
            return false;
        }

        return true;
    }

    private static class Constants{
        private final static Integer MY_INFO_PHONE_TYPE = 1;

        private final static Integer INACTIVATED_STATE = 0;//未激活
        private final static Integer USED_STATE = 1;//已经使用
        private final static Integer EXPIRE_STATE = 2;//过期
    }


}
