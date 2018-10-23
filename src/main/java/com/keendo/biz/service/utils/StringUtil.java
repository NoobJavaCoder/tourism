package com.keendo.biz.service.utils;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

    public static Boolean isNotEmpty(String str){
        return StringUtils.isNotEmpty(str);
    }

    public static Boolean isEmpty(String str){
        return StringUtils.isEmpty(str);
    }
}
