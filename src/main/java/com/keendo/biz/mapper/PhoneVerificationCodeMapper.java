package com.keendo.biz.mapper;

import com.keendo.biz.model.PhoneVerificationCode;
import org.apache.ibatis.annotations.Param;

/**
 * Created by bint on 2018/10/15.
 */
public interface PhoneVerificationCodeMapper {

    int insert(PhoneVerificationCode phoneVerificationCode);

    int updateState(@Param("id") Integer id , @Param("state") Integer state);

    PhoneVerificationCode selectNewestByPhoneNumberAndType(@Param("phoneNumber") String phoneNumber ,@Param("type") Integer type );
}
