package com.keendo.wxpay.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WXNotifyResp {
    @XStreamAlias("return_code")
    private String returnCode;
    @XStreamAlias("return_msg")
    private String returnMsg;

    public WXNotifyResp(){
    }

    public WXNotifyResp(String code,String msg){
        this.returnCode = code;
        this.returnMsg = msg;
    }

    public static WXNotifyResp ok(){
        return new WXNotifyResp("SUCCESS","OK");
    }

    public static WXNotifyResp fail(){
        return new WXNotifyResp("FAIL","FAIL");
    }


    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
