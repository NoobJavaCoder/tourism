package com.keendo.architecture.controller;

/**
 * Created by bint on 27/01/2018.
 */
public class RespHelper {

    public static RespBase ok(Object result) {
        return ok(result, null);
    }

    public static RespBase ok(Object result, Integer total) {

        RespBase resp = new RespBase(RetCode.SUCCESS);
        resp.setResult(result);
        resp.setTotal(total);

        return resp;
    }

    public static RespBase ok(Object result, Integer total, String retMsg) {

        RespBase resp = new RespBase(RetCode.SUCCESS);
        resp.setResult(result);
        resp.setTotal(total);
        resp.setRetMsg(retMsg);

        return resp;
    }

    public static RespBase ok() {
        RespBase resp = new RespBase(RetCode.SUCCESS);
        resp.setResult(null);

        return resp;
    }


    public static RespBase failed(String msg) {
        RespBase resp = new RespBase(RetCode.FAILED);
        resp.setRetMsg(msg);

        return resp;
    }


    public static RespBase nologin() {

        RespBase resp = new RespBase(RetCode.NO_LOGIN);
//        resp.setRetMsg(Msg.text("common.adminuser.nologin"));
        resp.setRetMsg("common adminuser nologin");
        return resp;
    }


}
