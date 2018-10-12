package com.keendo.biz.model;

import java.math.BigDecimal;
import java.util.Date;

public class TourProduct {
    private Integer id;
    private String title;
    private String coverImgUrl;
    private String tourSummary;
    private Date departureTime;
    private Integer tourDay;
    private BigDecimal price;
    private Date deadline;
    private Integer maxParticipantNum;
    private String wxPubUrl;
    private Date createTime;
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getTourSummary() {
        return tourSummary;
    }

    public void setTourSummary(String tourSummary) {
        this.tourSummary = tourSummary;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getTourDay() {
        return tourDay;
    }

    public void setTourDay(Integer tourDay) {
        this.tourDay = tourDay;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Integer getMaxParticipantNum() {
        return maxParticipantNum;
    }

    public void setMaxParticipantNum(Integer maxParticipantNum) {
        this.maxParticipantNum = maxParticipantNum;
    }

    public String getWxPubUrl() {
        return wxPubUrl;
    }

    public void setWxPubUrl(String wxPubUrl) {
        this.wxPubUrl = wxPubUrl;
    }
}
