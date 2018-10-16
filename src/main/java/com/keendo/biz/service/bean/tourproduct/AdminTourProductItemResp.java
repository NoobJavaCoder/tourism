package com.keendo.biz.service.bean.tourproduct;

import java.math.BigDecimal;
import java.util.Date;

/**
 * admin旅游产品参数
 */
public class AdminTourProductItemResp {
    private Integer id;
    private String title;
    private String tourSummary;
    private String coverImgUrl;
    private String posterUrl;
    private Integer tourDay;
    private BigDecimal price;
    private Integer hasPaidNum;//已付款人数
    private Integer unPaidNum;//未付款人数
    private String wxPubUrl;
    private Date departureTime;
    private Date deadline;
    private Integer maxParticipantNum;//人数上限
    private Date createTime;
    private Integer state;

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTourSummary() {
        return tourSummary;
    }

    public void setTourSummary(String tourSummary) {
        this.tourSummary = tourSummary;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getHasPaidNum() {
        return hasPaidNum;
    }

    public void setHasPaidNum(Integer hasPaidNum) {
        this.hasPaidNum = hasPaidNum;
    }

    public Integer getUnPaidNum() {
        return unPaidNum;
    }

    public void setUnPaidNum(Integer unPaidNum) {
        this.unPaidNum = unPaidNum;
    }

    public String getWxPubUrl() {
        return wxPubUrl;
    }

    public void setWxPubUrl(String wxPubUrl) {
        this.wxPubUrl = wxPubUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}
