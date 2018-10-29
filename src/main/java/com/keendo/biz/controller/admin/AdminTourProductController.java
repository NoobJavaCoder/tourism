package com.keendo.biz.controller.admin;

import com.keendo.architecture.controller.RespBase;
import com.keendo.architecture.controller.RespHelper;
import com.keendo.architecture.exception.BizException;
import com.keendo.biz.controller.base.bean.IdReq;
import com.keendo.biz.controller.base.bean.PageParamReq;
import com.keendo.biz.model.TourProduct;
import com.keendo.biz.service.TourProductService;
import com.keendo.biz.service.TourProductService.Constants;
import com.keendo.biz.service.bean.UploadFile;
import com.keendo.biz.service.bean.tourproduct.AddTourProduct;
import com.keendo.biz.service.bean.tourproduct.AdminTourProductItemResp;
import com.keendo.biz.service.bean.tourproduct.AdminTourProductListItemResp;
import com.keendo.biz.service.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;
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
        Date now = new Date();

        Date departureTime = addTourProduct.getDepartureTime();
        if(departureTime == null || now.compareTo(departureTime) >= 0){
            return RespHelper.failed("旅行出发时间填写有误");
        }

        //报名截止日期应该在启程时间之前
        Date deadline = addTourProduct.getDeadline();
        if(deadline == null || now.compareTo(deadline) >= 0 || deadline.compareTo(departureTime) >= 0){
            return RespHelper.failed("报名截止日期填写有误");
        }

        Integer maxParticipantNum = addTourProduct.getMaxParticipantNum();
        if(maxParticipantNum == null || maxParticipantNum <= 0){
            return RespHelper.failed("最大人数上限填写有误");
        }

        BigDecimal price = addTourProduct.getPrice();
        if(price == null || price.compareTo(new BigDecimal("0")) <= 0){
            return RespHelper.failed("人均价格填写有误");
        }

        String title = addTourProduct.getTitle();
        if(StringUtil.isEmpty(title)){
            return RespHelper.failed("产品标题不能为空");
        }

        Integer tourDay = addTourProduct.getTourDay();
        if(tourDay == null || tourDay <= 0){
            return RespHelper.failed("旅行天数填写有误");
        }

        String tourSummary = addTourProduct.getTourSummary();
        if(StringUtil.isEmpty(tourSummary)){
            return RespHelper.failed("行程概要不能为空");
        }

        String coverImgUrl = addTourProduct.getCoverImgUrl();
        if(StringUtil.isEmpty(coverImgUrl)){
            return RespHelper.failed("产品封面图不能为空");
        }

        String posterUrl = addTourProduct.getPosterUrl();
        if(StringUtil.isEmpty(posterUrl)){
            return RespHelper.failed("分享海报不能为空");
        }

        String topPosterUrl = addTourProduct.getTopPosterUrl();
        if(StringUtil.isEmpty(topPosterUrl)){
            return RespHelper.failed("顶部海报不能为空");
        }

        tourProductService.add(addTourProduct);

        return RespHelper.ok();
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RespBase update(@RequestBody TourProduct tourProduct) {
        Date departureTime = tourProduct.getDepartureTime();
        if(departureTime == null ){
            throw new BizException("旅行出发时间填写有误");
        }

        //报名截止日期应该在启程时间之前
        Date deadline = tourProduct.getDeadline();
        if(deadline == null  || deadline.compareTo(departureTime) >= 0){
            throw new BizException("报名截止日期填写有误");
        }

        Integer maxParticipantNum = tourProduct.getMaxParticipantNum();
        if(maxParticipantNum == null || maxParticipantNum <= 0){
            return RespHelper.failed("最大人数上限填写有误");
        }

        BigDecimal price = tourProduct.getPrice();
        if(price == null || price.compareTo(new BigDecimal("0")) <= 0){
            return RespHelper.failed("人均价格填写有误");
        }

        String title = tourProduct.getTitle();
        if(StringUtil.isEmpty(title)){
            return RespHelper.failed("产品标题不能为空");
        }

        Integer tourDay = tourProduct.getTourDay();
        if(tourDay == null || tourDay <= 0){
            return RespHelper.failed("旅行天数填写有误");
        }

        String tourSummary = tourProduct.getTourSummary();
        if(StringUtil.isEmpty(tourSummary)){
            return RespHelper.failed("行程概要不能为空");
        }

        String coverImgUrl = tourProduct.getCoverImgUrl();
        if(StringUtil.isEmpty(coverImgUrl)){
            return RespHelper.failed("产品封面图不能为空");
        }

        String posterUrl = tourProduct.getPosterUrl();
        if(StringUtil.isEmpty(posterUrl)){
            return RespHelper.failed("分享海报不能为空");
        }

        String topPosterUrl = tourProduct.getTopPosterUrl();
        if(StringUtil.isEmpty(topPosterUrl)){
            return RespHelper.failed("顶部海报不能为空");
        }

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
     * 上传旅游产品封面图
     *
     * @param uploadFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/cover-img", method = RequestMethod.POST)
    public RespBase addCoverImg(UploadFile uploadFile){

        String url = tourProductService.uploadPic(uploadFile.getMultipartFile(), Constants.COVER_DIRECTORY_NAME);

        return RespHelper.ok(url);
    }

    /**
     * 上传旅游分享海报
     *
     * @param uploadFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/share-poster", method = RequestMethod.POST)
    public RespBase uploadPoster1(UploadFile uploadFile) {

        String url = tourProductService.uploadPic(uploadFile.getMultipartFile(), Constants.POSTER1_DIRECTORY_NAME);

        return RespHelper.ok(url);
    }

    /**
     * 上传旅游分享海报
     *
     * @param uploadFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload/topbar-poster", method = RequestMethod.POST)
    public RespBase uploadPoster2(UploadFile uploadFile) {

        String url = tourProductService.uploadPic(uploadFile.getMultipartFile(), Constants.POSTER2_DIRECTORY_NAME);

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
