package com.keendo.biz.service.bean.tourproduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*首页旅游产品*/
public class TourProductItem {
    private Integer id;//产品id
    private String tourSummary;//行程概要
    private String title;//产品标题
    private String coverImgUrl;//封面图
    private Integer tourDay;//出游天数
    private Date deadline;//报名截止日期
    private Date departureTime;//启程时间
    private Date endTime;//结束时间
    private BigDecimal price = new BigDecimal("0");//价格
    private List<String> headImgList = new ArrayList<>();//参团者头像
    private Integer hasOrderedNum;//已下单人数
    private Integer remainNum;//剩余人数
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public Integer getTourDay() {
        return tourDay;
    }

    public void setTourDay(Integer tourDay) {
        this.tourDay = tourDay;
    }

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

}
