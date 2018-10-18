package com.keendo.biz.controller.app;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.biz.controller.base.bean.IdReq;
import com.keendo.biz.controller.base.bean.PageParamReq;
import com.keendo.biz.service.TourProductService;
import com.keendo.biz.service.bean.tourproduct.TourProductItem;
import com.keendo.biz.service.bean.tourproduct.TourProductItemDetail;
import com.keendo.user.controlller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/app/tour-product")
public class AppTourProductController extends BaseController{

    @Autowired
    private TourProductService tourProductService;

    /**
     * 旅游产品首页
     * @param pageParamReq
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public RespBase getTourProductList(@RequestBody PageParamReq pageParamReq){

        Integer startIndex = pageParamReq.getStartIndex();
        Integer pageSize = pageParamReq.getPageSize();

        List<TourProductItem> items = tourProductService.getAppTourProductItemList(startIndex, pageSize);

        Integer count = tourProductService.indexCount();

        return RespHelper.ok(items,count);
    }

    /**
     * 旅游产品详情
     * @param idReq
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RespBase getTourProductDetail(@RequestBody IdReq idReq){

        Integer tourProductId = idReq.getId();

        TourProductItemDetail appTourProductDetail = tourProductService.getAppTourProductDetail(tourProductId);

        return RespHelper.ok(appTourProductDetail);
    }


}
