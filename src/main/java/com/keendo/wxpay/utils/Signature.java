package com.keendo.wxpay.utils;

import com.keendo.wxpay.constant.Configure;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Signature {

    /**
     * 签名算法
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    public static String getSign(Object o) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                String name = f.getName();
                XStreamAlias anno = f.getAnnotation(XStreamAlias.class);
                if(anno != null)
                    name = anno.value();
                list.add(name + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + Configure.getKey();
        System.out.println("签名数据："+result);
        result = MD5.MD5Encode(result).toUpperCase();
        return result;
    }

    public static String getSign(Map map){
        ArrayList<String> list = new ArrayList<>();
        for(Object key:map.keySet()){
            Object v = map.get(key);
            if(null != v && !"".equals(v) && !"sign".equals(key)){
                list.add(key + "=" + map.get(key) + "&");
            }
        }
//        for(Map.Entry<String,String> entry:map.entrySet()){
//            if(entry.getValue()!=""){
//                list.add(entry.getKey() + "=" + entry.getValue() + "&");
//            }
//        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + Configure.getKey();
        //Util.log("Sign Before MD5:" + result);
        result = MD5.MD5Encode(result).toUpperCase();
        //Util.log("Sign Result:" + result);
        return result;
    }

}
