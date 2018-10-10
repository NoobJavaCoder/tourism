package com.keendo.user.service.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {

    /**
     * 中国公民身份证号码最大长度。
     */
    private static final int CHINA_ID_MAX_LENGTH = 18;

    /**
     * 身份证号码前17位每位对应加权因子。
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 校验目标字符串是否符合指定的正则表达式规则。
     *
     * @param regex 正则表达式
     * @param value 目标字符串
     * @return 校验结果
     */
    public static boolean isLegalString(String regex, String value){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * 验证目标字符串是否为整数。
     *
     * @param value 目标字符串
     * @return true:整数 false:非整数
     */
    public static boolean isInteger(String value){
        try{
            if(value == null || value.isEmpty()){
                return false;
            }
            Integer.parseInt(value);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    /**
     * 验证目标字符串是否为数字。
     *
     * @param value 目标字符串
     * @return true:数字 false:非数字
     */
    public static boolean isNumber(String value){
        if(StringUtils.isBlank(value)){
            return false;
        }
        // 数字正则表达式
        String regex = "^[0-9]*$";
        return isLegalString(regex, value);
    }


    /**
     * 验证通行证格式合法性（旧用户规则）。规则：4-20个字符，支持英文字母、数字以及下划线。
     *
     * @param passport 通行证
     * @return true:合法 false:不合法
     */
    public static boolean isPassport(String passport){
        if(StringUtils.isBlank(passport)){
            return false;
        }
        // 通行证正则表达式
        String regex = "^\\w{4,20}$";
        return isLegalString(regex, passport);
    }

    /**
     * 验证密码合法性。规则：6-16个字符，支持英文字母、数字以及下划线，区分大小写。
     *
     * @param password 密码
     * @return true:合法 false:不合法
     */
    public static boolean isPassword(String password){
        if(StringUtils.isBlank(password)){
            return false;
        }
        // 密码正则表达式
        String regex = "^\\w{6,16}$";
        return isLegalString(regex, password);
    }

    /**
     * 验证邮箱地址格式合法性。规则：5-32个字符，只能以英文字母或者数字开头，支持英文字母、数字、下划线、@、小数点以及中划线。
     *
     * @param email 邮箱地址
     * @return true:合法 false:不合法
     */
    public static boolean isEmail(String email){
        if(StringUtils.isBlank(email) || email.length() > 32){
            return false;
        }
        // 邮箱地址正则表达式
        String regex = "(?!_)\\w+([-.]\\w+)*@(\\w+)(\\.\\w+){1,2}";
        return isLegalString(regex, email);
    }

    /**
     * 验证手机号码合法性。
     *
     * @param phone 手机号码
     * @return true:合法 false:不合法
     */
    public static boolean isPhone(String phone){
        if(StringUtils.isBlank(phone)){
            return false;
        }
        // 手机号码正则表达式
        String regex = "^((13[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
        return isLegalString(regex, phone);
    }


    /**
     * 验证手机号码短信码
     *
     * @param phoneCode 手机号码短信码
     * @return true:合法 false:不合法
     */
    public static boolean isPhoneCode(String phoneCode){
        if(StringUtils.isBlank(phoneCode)){
            return false;
        }
        return isNumber(phoneCode) && phoneCode.length() == 6;
    }

    /**
     * 验证昵称合法性。规则：5-15个字符，支持汉字、字母、数字或下划线，不能用数字开头。
     *
     * @param nickName 昵称
     * @return true:合法 false:不合法
     */
    public static boolean isNickName(String nickName){
        if(StringUtils.isBlank(nickName)){
            return false;
        }
        // 昵称正则表达式
        String regex = "^[^0-9][a-zA-Z0-9_\u4e00-\u9fa5]{4,14}$";
        return isLegalString(regex, nickName);
    }

    /**
     * 验证真实姓名合法性。规则：2-8个汉字。
     *
     * @param realName 真实姓名
     * @return true:合法 false:不合法
     */
    public static boolean isRealName(String realName){
        if(StringUtils.isBlank(realName)){
            return false;
        }
        // 真实姓名正则表达式
        String regex = "^[\u4e00-\u9fa5]{2,8}$";
        return isLegalString(regex, realName);
    }

    /**
     * 验证IP地址合法性。
     *
     * @param ip IP地址
     * @return true:合法 false:不合法
     */
    public static boolean isIp(String ip){
        if(StringUtils.isBlank(ip)){
            return false;
        }
        // IP正则表达式
        String regex = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
        return isLegalString(regex, ip);
    }

    /**
     * 验证渠道合法性。
     *
     * @param channel 渠道
     * @return true:合法 false:不合法
     */
    public static boolean isChannel(String channel){
        if(StringUtils.isBlank(channel)){
            return false;
        }
        // 数字正则表达式
        String regex = "^[0-9]*$";
        return isLegalString(regex, channel);
    }

    /**
     * 验证来源合法性。规则：1-20个字符，支持英文字母、数字以及下划线。
     *
     * @param from 来源
     * @return true:合法 false:不合法
     */
    public static boolean isFrom(String from){
        if(StringUtils.isBlank(from)){
            return false;
        }
        // 来源正则表达式
        String regex = "^\\w{1,20}$";
        return isLegalString(regex, from);
    }

    /**
     * 验证邮政编码合法性。规则：中国邮政编码为6位数字。
     *
     * @param zip 邮政编码
     * @return true:合法 false:不合法
     */
    public static boolean isZip(String zip){
        if(StringUtils.isBlank(zip)){
            return false;
        }
        // 邮政编码正则表达式
        String regex = "\\d{6}";
        return isLegalString(regex, zip);
    }

    /**
     * 验证QQ号合法性。规则：腾讯QQ号从10000开始。
     *
     * @param qq qq号
     * @return true:合法 false:不合法
     */
    public static boolean isQq(String qq){
        if(StringUtils.isBlank(qq)){
            return false;
        }
        // QQ号正则表达式
        String regex = "[1-9][0-9]{4,}";
        return isLegalString(regex, qq);
    }

    /**
     * 验证身份证号码是否合法。如果是18位的身份证，则校验18位的身份证。15的身份证不校验，也无法校验。
     *
     * @param idCard 身份证号码
     * @return true:合法 false:不合法
     */
    public static boolean isIdCard(String idCard){
        if(StringUtils.isBlank(idCard)){
            return false;
        }

        // 15位或者18位身份证正则表达式
        String regex = "\\d{15}|\\d{17}[x,X,0-9]";
        if(!isLegalString(regex, idCard)){
            return false;
        }

        // 如果是18位的身份证，则校验18位的身份证，15的身份证暂不校验。
        if(idCard.length() == CHINA_ID_MAX_LENGTH){
            // 前17位
            String code17 = idCard.substring(0, 17);
            // 第18位
            String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
            if(isNumber(code17)){
                char[] charArr = code17.toCharArray();
                if(charArr != null){
                    int[] intArr = convertCharToInt(charArr);
                    int iSum17 = getPowerSum(intArr);
                    // 获取校验位
                    String value = getCheckCode(iSum17);
                    if(code18.equalsIgnoreCase(value)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 将字符数组转换成数字数组。
     *
     * @param charArr 字符数组
     * @return 数字数组
     */
    private static int[] convertCharToInt(char[] charArr){
        int len = charArr.length;
        int[] intArr = new int[len];
        try{
            for(int i = 0; i < len; i++){
                intArr[i] = Integer.parseInt(String.valueOf(charArr[i]));
            }
        }catch(NumberFormatException e){
            e.printStackTrace();
        }
        return intArr;
    }

    /**
     * 获取身份证的每位和对应位的加权因子相乘之后的和值。
     *
     * @param intArr 数字数组
     * @return 身份证号码权值
     */
    private static int getPowerSum(int[] intArr){
        int iSum = 0;
        if(POWER.length == intArr.length){
            for(int i = 0; i < intArr.length; i++){
                iSum = iSum + intArr[i] * POWER[i];
            }
        }
        return iSum;
    }

    /**
     * 通过身份证号码前17位权值与11取模获得的余数判断，获取身份证最后一位校验码。
     *
     * @param iSum 身份证号码前17位权值
     * @return 身份证号码最后一位校验位
     */
    private static String getCheckCode(int iSum){
        String sCode = "";
        switch(iSum % 11){
            case 10:
                sCode = "2";
                break;
            case 9:
                sCode = "3";
                break;
            case 8:
                sCode = "4";
                break;
            case 7:
                sCode = "5";
                break;
            case 6:
                sCode = "6";
                break;
            case 5:
                sCode = "7";
                break;
            case 4:
                sCode = "8";
                break;
            case 3:
                sCode = "9";
                break;
            case 2:
                sCode = "X";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
        }
        return sCode;
    }

    public static void main(String [] args){
        String name = "淼";
        boolean realName = isRealName(name);
        System.out.println(realName);
    }
}
