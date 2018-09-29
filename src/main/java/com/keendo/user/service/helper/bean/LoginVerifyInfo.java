package com.keendo.user.service.helper.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginVerifyInfo {
    @JsonProperty("openid")
    private String opneId;
    @JsonProperty("session_key")
    private String sessionKey;
    @JsonProperty("unionid")
    private String unionId;

    public String getOpneId() {
        return opneId;
    }

    public void setOpneId(String opneId) {
        this.opneId = opneId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
