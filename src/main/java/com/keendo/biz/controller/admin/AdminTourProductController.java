package com.keendo.biz.controller.admin;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.controller.admin.utils.AdminLoginUtils;
import com.keendo.biz.controller.base.bean.IdReq;
import com.keendo.biz.controller.base.bean.PageParamReq;
import com.keendo.biz.model.TourProduct;
import com.keendo.biz.service.TourProductService;
import com.keendo.biz.service.bean.UploadFile;
import com.keendo.biz.service.bean.tourproduct.AddTourProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/tour-product")
public class AdminTourProductController {

    @Autowired
    private TourProductService tourProductService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public RespBase getAuctionItemPage(@RequestBody PageParamReq pageParamReq){
        Boolean login = AdminLoginUtils.isLogin();
        if(!login){
            return RespHelper.nologin();
        }

        Integer startIndex = pageParamReq.getStartIndex();
        Integer pageSize = pageParamReq.getPageSize();

        List<TourProduct> listByPage = tourProductService.getListByPage(startIndex, pageSize);

        Integer total = tourProductService.count();

        return RespHelper.ok(listByPage,total);
    }


    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public RespBase get(@RequestBody IdReq idReq){
        Boolean login = AdminLoginUtils.isLogin();
        if(!login){
            return RespHelper.nologin();
        }

        Integer id = idReq.getId();
        TourProduct tp = tourProductService.getById(id);
        return RespHelper.ok(tp);
    }


    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBase add(@RequestBody AddTourProduct addTourProduct){
        Boolean login = AdminLoginUtils.isLogin();
        if(!login){
            return RespHelper.nologin();
        }

        tourProductService.add(addTourProduct);
        return RespHelper.ok();
    }


    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RespBase update(@RequestBody TourProduct tourProduct){
        Boolean login = AdminLoginUtils.isLogin();
        if(!login){
            return RespHelper.nologin();
        }

        tourProductService.update(tourProduct);
        return RespHelper.ok();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespBase deleteById(@RequestBody IdReq idReq){
        Boolean login = AdminLoginUtils.isLogin();
        if(!login){
            return RespHelper.nologin();
        }

        Integer id = idReq.getId();
        tourProductService.deleteById(id);
        return RespHelper.ok();
    }


    /**
     * 上传旅游产品封面图
     * @param uploadFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/cover-img", method = RequestMethod.POST)
    public RespBase addCoverImg(UploadFile uploadFile){

        String url = tourProductService.uploadPic(uploadFile.getMultipartFile());

        return RespHelper.ok(url);
    }

}
