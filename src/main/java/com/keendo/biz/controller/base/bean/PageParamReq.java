package com.keendo.biz.controller.base.bean;

/**
 * Created by bint on 2018/9/12.
 */
public class PageParamReq {

    private Integer page;
    private Integer pageSize;


    public Integer getStartIndex(){
        return (page - 1) * pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
