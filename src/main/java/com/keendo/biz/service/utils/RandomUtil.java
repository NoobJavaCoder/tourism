package com.keendo.biz.service.utils;


import com.keendo.architecture.utils.Log;

import java.util.Random;

/**
 * Created by bint on 2018/8/20.
 */
public class RandomUtil {

    /**
     * 生成随机字符串
     *
     * @param length 生成字符串长度
     * @return
     */
    public static String getRandomString(int length) {
        String baseChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(baseChar.length());
            sb.append(baseChar.charAt(number));
        }

        return sb.toString();
    }

    // nextInt取值:[0,x)
    public static int randomIndex(int size){
        Random random = new Random();
        return random.nextInt(size);
    }

    public static String radomNumber(Integer length){

        String str = "";

        for(int i=0;i<length;i++){
            int index = randomIndex(9);
            str = str + String.valueOf(index);
        }

        return str;
    }

    public static void main(String[] args) {
        String radomNumber = radomNumber(6);

        Log.d(radomNumber);
    }
}
