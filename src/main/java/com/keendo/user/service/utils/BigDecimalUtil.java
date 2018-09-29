package com.keendo.user.service.utils;

import java.math.BigDecimal;

/**
 * Created by bint on 15/03/2018.
 */
public class BigDecimalUtil {


    public static BigDecimal getInstance(String str) {
        return new BigDecimal(str);
    }


    public static BigDecimal getInstance(Float f) {

        return getInstance(f.toString());

    }


    public static BigDecimal getInstance(Double d) {
        return new BigDecimal(d.toString());
    }


    public static BigDecimal getInstance(Integer i) {
        return new BigDecimal(i.toString());
    }

    public static BigDecimal divide(BigDecimal divisor, BigDecimal divide, Integer scale) {

        if (divide.floatValue() == 0) {
            return null;
        }

        BigDecimal result = divisor.divide(divide, scale, BigDecimal.ROUND_HALF_UP);


        return result;
    }
}
