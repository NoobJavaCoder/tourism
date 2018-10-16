package com.keendo.biz.service.utils;


import com.keendo.architecture.utils.Log;

import java.util.Random;

/**
 * Created by bint on 2018/8/20.
 */
public class RandomUtil {
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
