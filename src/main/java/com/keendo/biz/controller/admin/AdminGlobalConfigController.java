package com.keendo.biz.controller.admin;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.model.GlobalConfig;
import com.keendo.biz.service.CfgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/global-config")
public class AdminGlobalConfigController {

    @Autowired
    private CfgService cfgService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespBase list(){

        List<GlobalConfig> globalConfigs = cfgService.selectAll();

        return RespHelper.ok(globalConfigs);
    }
}
