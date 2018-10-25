package com.keendo.biz.service.bean.tourproduct;

import java.util.ArrayList;
import java.util.List;

public class TourProductItemDetail {
    private String posterUrl;
    private String topPosterUrl;
    private List<String> headImgList = new ArrayList<>();
    private Integer hasOrderedNum;//已下单人数
    private Integer remainNum;//剩余人数
    private String wxPubUrl;//公众号链接
    private Integer state;
    private String title;
    private Integer orderState;
    private String orderSn;

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTopPosterUrl() {
        return topPosterUrl;
    }

    public void setTopPosterUrl(String topPosterUrl) {
        this.topPosterUrl = topPosterUrl;
    }

    public List<String> getHeadImgList() {
        return headImgList;
    }

    public void setHeadImgList(List<String> headImgList) {
        this.headImgList = headImgList;
    }

    public Integer getHasOrderedNum() {
        return hasOrderedNum;
    }

    public void setHasOrderedNum(Integer hasOrderedNum) {
        this.hasOrderedNum = hasOrderedNum;
    }

    public Integer getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(Integer remainNum) {
        this.remainNum = remainNum;
    }

    public String getWxPubUrl() {
        return wxPubUrl;
    }

    public void setWxPubUrl(String wxPubUrl) {
        this.wxPubUrl = wxPubUrl;
    }
}
