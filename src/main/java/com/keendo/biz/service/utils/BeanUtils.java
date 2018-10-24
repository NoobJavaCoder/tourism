package com.keendo.biz.service.utils;


import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bint on 2017/12/5.
 */

public class BeanUtils {

    /**
     * 将source里面的属性赋给target
     * @param source
     * @param target
     */
    public static void copyProperties(Object source , Object target){

        /*Map<String,Field> targetMap = new HashMap<String,Field>();

        Field[] targetFields = target.getClass().getDeclaredFields();
        for(int i=0;i<targetFields.length;i++){
            String targetFieldName = targetFields[i].getName();
            targetMap.put(targetFieldName, targetFields[i]);
        }


        Field[] sourceFields = source.getClass().getDeclaredFields();

        for(int i=0;i<sourceFields.length;i++){

            Field sourceField = sourceFields[i];

            String sourceFieldName = sourceFields[i].getName();

            Boolean isExist = targetMap.containsKey(sourceFieldName);
            if(isExist){
                //取消安全检查
                Field targetField = targetMap.get(sourceFieldName);
                sourceField.setAccessible(true);
                targetField.setAccessible(true);

                try {
                    Object valueObject = sourceField.get(source);
                    targetField.set(target, valueObject);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                sourceField.setAccessible(false);
                targetField.setAccessible(false);
            }

        }*/

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


    /*public static void main(String[] args) {

        UpdateAd updateAd = new UpdateAd();

        updateAd.setDescription("aaaaaa");

        AdUpdateReq adUpdateReq = getBean(updateAd, AdUpdateReq.class);

        System.out.println(adUpdateReq.toString());

    }*/


    public static void setFieldValue(Field field , Object object , Object value){

        if(object == null){
            return ;
        }

        field.setAccessible(true);
        try {
            field.set(object,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        field.setAccessible(false);

    }

    /**
     * 获取到对象中为null的属性名
     * @param source 要拷贝的对象
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 拷贝非空对象属性值
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
}
