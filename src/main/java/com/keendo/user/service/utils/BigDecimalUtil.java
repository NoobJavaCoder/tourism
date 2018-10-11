package com.keendo.user.service.utils;

import java.math.BigDecimal;

/**
 * Created by bint on 15/03/2018.
 */
public class BigDecimalUtil {

    private static final Integer SYS_DEFAULT_SCALE = 3;


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

    public static BigDecimal add(BigDecimal b1,BigDecimal b2){
        return b1.add(b2);
    }

    public static BigDecimal sub(BigDecimal b1,BigDecimal b2){
        return  b1.subtract(b2);
    }

    public static BigDecimal mul(BigDecimal b1,BigDecimal b2){
        return b1.multiply(b2);
    }

    public static BigDecimal div(BigDecimal divisor, BigDecimal divide) {

        if (divide.floatValue() == 0) {
            return null;
        }

        BigDecimal result = divisor.divide(divide, SYS_DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);

        return result;
    }

    public static void main(String [] args){
    }
}
