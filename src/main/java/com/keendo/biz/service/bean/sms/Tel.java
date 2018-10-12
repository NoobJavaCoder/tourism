package com.keendo.biz.service.bean.sms;

public class Tel {
    private String mobile;
    private String nationcode;


    public Tel() {
    }

    public Tel(String mobile, String nationcode) {
        this.mobile = mobile;
        this.nationcode = nationcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNationcode() {
        return nationcode;
    }

    public void setNationcode(String nationcode) {
        this.nationcode = nationcode;
    }
}
