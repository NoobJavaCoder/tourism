package com.keendo.wxpay.constant;

public enum TradeStateEnum {

    FAIL(-1,"FAIL","支付失败"),SUCCESS(1,"SUCCESS","成功"),PAYING(2,"USERPAYING","正在进行"),REFUND(3,"REFUND","转入退款");

    private Integer state;//交易状态
    private String code;//交易状态代码
    private String msg;//描述信息

    TradeStateEnum(Integer state,String code,String msg){
        this.state = state;
        this.code = code;
        this.msg = msg;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
