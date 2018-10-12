package com.keendo.biz.service.bean.sms;

import com.github.qcloudsms.SmsSingleSenderResult;

/**
 * api未提供getter方法,因此重写
 *
 * 参数介绍如下:
 * 参数	    必选	    类型	        描述
 * result	是	    number	    错误码，0 表示成功(计费依据)，非 0 表示失败，参考 错误码
 * errmsg	是	    string	    错误消息，result 非 0 时的具体错误信息
 * ext	    否	    string	    用户的 session 内容，腾讯 server 回包中会原样返回
 * fee	    否	    number	    短信计费的条数，"fee" 字段计费说明
 * sid	    否	    string	    本次发送标识 id，标识一次短信下发记录
 */
public class KdSmsSingleSenderResult extends SmsSingleSenderResult {

    /**
     * 0表示成功,其余失败
     * @return
     */
    public int getResult() {
        return result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getExt() {
        return ext;
    }

    public String getSid() {
        return sid;
    }

    public int getFee() {
        return fee;
    }


}
