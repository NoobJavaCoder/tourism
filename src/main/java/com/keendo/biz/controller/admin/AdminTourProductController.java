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
import com.keendo.biz.service.bean.tourproduct.AdminTourProductItemResp;
import com.keendo.biz.service.bean.tourproduct.AdminTourProductListItemResp;
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
    public RespBase getAuctionItemPage(@RequestBody PageParamReq pageParamReq) {

        Integer startIndex = pageParamReq.getStartIndex();
        Integer pageSize = pageParamReq.getPageSize();

        List<AdminTourProductListItemResp> listByPage = tourProductService.getListByPage(startIndex, pageSize);

        Integer total = tourProductService.count();

        return RespHelper.ok(listByPage, total);
    }


    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public RespBase get(@RequestBody IdReq idReq) {
        Integer id = idReq.getId();
        AdminTourProductItemResp resp = tourProductService.getAdminTourProductItemById(id);
        return RespHelper.ok(resp);
    }


    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBase add(@RequestBody AddTourProduct addTourProduct) {
        tourProductService.add(addTourProduct);
        return RespHelper.ok();
    }


    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RespBase update(@RequestBody TourProduct tourProduct) {

        tourProductService.update(tourProduct);
        return RespHelper.ok();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public RespBase deleteById(@RequestBody IdReq idReq) {

        Integer id = idReq.getId();
        tourProductService.deleteById(id);
        return RespHelper.ok();
    }


    /**
     * 上传旅游产品封面图 .
     *
     * @param uploadFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/cover-img", method = RequestMethod.POST)
    public RespBase addCoverImg(UploadFile uploadFile) {

        String url = tourProductService.uploadPic(uploadFile.getMultipartFile());

        return RespHelper.ok(url);
    }

    /**
     * 下架
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unshelve", method = RequestMethod.POST)
    public RespBase unshelve(@RequestBody IdReq idReq) {
        Integer id = idReq.getId();

        tourProductService.updateStateById(id, TourProductService.Constants.UNSHELVE_STATE);

        return RespHelper.ok();
    }

}
