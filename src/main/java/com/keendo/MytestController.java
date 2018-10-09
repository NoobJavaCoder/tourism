package com.keendo;

import com.keendo.user.service.utils.CommonUtils;

import java.util.Date;

public class MytestController {
    public static void main(String[]args){
        Date departureTime = CommonUtils.dateOffset(new Date(), 7);
        long timeStamp = departureTime.getTime();
        System.out.println(timeStamp);
    }
}
