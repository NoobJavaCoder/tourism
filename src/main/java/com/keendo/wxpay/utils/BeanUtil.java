package com.keendo.wxpay.utils;

/**
 * Created by bint on 2017/12/5.
 */
public class BeanUtil {

    /**
     * 将source里面的属性赋给target
     * @param source
     * @param target
     */
    public static void copyProperties(Object source , Object target){

        org.springframework.beans.BeanUtils.copyProperties(source, target);

    }

    /**
     * 复制一份指定的类
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T copyBean(Object source  , Class<T> clazz){

        T newInstance = null;
        try {
            newInstance = (T)clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        copyProperties(source,newInstance);
        return newInstance;
    }
}
