package com.keendo.wxpay.utils;


import com.keendo.wxpay.bean.MiniAppPayParam;
import com.keendo.wxpay.bean.PaySignature;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class WXPayUtil {


    /**
     * 生成签名
     *
     * @param o:签名对象
     * @param key:秘钥
     * @return
     * @throws Exception
     */
    public static String getSign(Object o, String key) throws Exception {
        return Signature.getSign(o, key);
    }

    /**
     * 生成签名
     *
     * @param xmlRet:xml
     * @param key:秘钥
     * @return
     * @throws Exception
     */
    public static String getSign(String xmlRet, String key) throws Exception {
        Map map = XmlBeanUtil.doXMLParse(xmlRet);
        String sign = Signature.getSign(map, key);
        return sign;
    }

    /**
     * 验证签名
     *
     * @param xmlRet:xml
     * @param key:秘钥
     * @return
     * @throws Exception
     */
    public static boolean checkSign(String xmlRet, String key) throws Exception {
        String geneSign = getSign(xmlRet, key);
        String signFromXml = getSignFromXml(xmlRet);
        return geneSign.equals(signFromXml);
    }

    private static String getSignFromXml(String xmlRet) throws Exception {
        Map map = XmlBeanUtil.doXMLParse(xmlRet);
        String sign = (String) map.get("sign");
        return sign;
    }

    /**
     * 从支付回调信息中获取resultCode
     *
     * @param xmlRet
     * @return
     * @throws Exception
     */
    public static String getResultCode(String xmlRet) throws Exception {
        Map map = XmlBeanUtil.doXMLParse(xmlRet);
        String resultCode = (String) map.get("result_code");
        return resultCode;
    }

    /**
     * 生成小程序支付参数对象
     *
     * @return
     */
    public static MiniAppPayParam createMiniAppPayParam(String appId, String nonceStr, String prepayId, String key) throws Exception {
        PaySignature paySignature = new PaySignature(appId, nonceStr, prepayId);

        MiniAppPayParam miniAppPayParam = BeanUtil.copyBean(paySignature, MiniAppPayParam.class);

        String paySign = getSign(paySignature, key);

        miniAppPayParam.setPaySign(paySign);

        return miniAppPayParam;
    }

    /**
     * 获取32位随机字符串作为nonceStr
     *
     * @return
     */
    public static String createNonceStr() {
        return getRandomString(32);
    }

    /**
     * 生成系统订单号
     *
     * @return
     */
    public static String createOrderSn() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 用于回调
     * 读请求数据
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static String readXml(HttpServletRequest request) throws Exception {
        ServletInputStream is = request.getInputStream();
        String callbackXml = StreamUtil.read(is);
        return callbackXml;
    }

    /**
     * 用于回调响应数据
     * 写数据
     *
     * @param response
     * @param xmlStr
     * @throws Exception
     */
    public static void writeXml(HttpServletResponse response, String xmlStr) throws Exception {
        PrintWriter writer = response.getWriter();
        writer.print(xmlStr);
    }


    /**
     * 获取服务器ip
     *
     * @return
     * @throws Exception
     */
    public static String getCreateIp() throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();
        return hostAddress;
    }

    /**
     * 将"元"转为"分"
     *
     * @param yuanAmount
     * @return
     */
    public static Integer getTotalFee(BigDecimal yuanAmount) {
        return yuanAmount.multiply(new BigDecimal(100)).intValue();
    }


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


}
