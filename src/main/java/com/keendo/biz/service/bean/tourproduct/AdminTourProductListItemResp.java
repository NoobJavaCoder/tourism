package com.keendo.biz.service.bean.tourproduct;

import java.math.BigDecimal;
import java.util.Date;

/**
 * admin旅游产品列表项参数
 */
public class AdminTourProductListItemResp {
    private Integer id;
    private String title;
    private Date departureTime;
    private Date deadline;
    private Integer maxParticipantNum;//人数上限
    private Date createTime;
    private Integer state;
    private Integer hasEnteredNum;//已报名人数

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

    public Integer getHasEnteredNum() {
        return hasEnteredNum;
    }

    public void setHasEnteredNum(Integer hasEnteredNum) {
        this.hasEnteredNum = hasEnteredNum;
    }
}
