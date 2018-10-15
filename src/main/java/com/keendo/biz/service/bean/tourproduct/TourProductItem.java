package com.keendo.biz.service.bean.tourproduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.xml.internal.rngom.binary.DataExceptPattern;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/*首页旅游产品*/
public class TourProductItem {
    private Integer id;
    private String tourSummary;
    private String title;
    @JsonFormat(pattern="yyyy年MM月dd日",timezone = "GMT+8")
    private Date deadline;
    @JsonFormat(pattern="yyyy年MM月dd日",timezone = "GMT+8")
    private Date departureTime;
    @JsonFormat(pattern="yyyy年MM月dd日",timezone = "GMT+8")
    private Date endTime;
    private BigDecimal price = new BigDecimal("0");
    private List<String> headImgList = new ArrayList<>();
    private Integer hasOrderedNum;
    private Integer remainNum;
    private String wxPubUrl;

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTourSummary() {
        return tourSummary;
    }

    public void setTourSummary(String tourSummary) {
        this.tourSummary = tourSummary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
