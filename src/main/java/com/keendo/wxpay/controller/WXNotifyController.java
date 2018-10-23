package com.keendo.wxpay.controller;

import com.keendo.wxpay.bean.WXNotifyResp;
import com.keendo.wxpay.service.WXPayKitService;
import com.keendo.wxpay.utils.Log;
import com.keendo.wxpay.utils.WXPayUtil;
import com.keendo.wxpay.utils.XmlBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/wx-pay")
public class WXNotifyController {

    @Autowired
    private WXPayKitService wxPayKitService;


    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public void callback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String xmlRet = WXPayUtil.readXml(request);

        WXNotifyResp resp = wxPayKitService.callback(xmlRet);

        String respXml = XmlBeanUtil.toXmlWithCData(resp);

        WXPayUtil.writeXml(response, respXml);
    }


}
