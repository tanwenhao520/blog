package com.tan.blog.common;

import java.util.List;

public class PageResult<T> {

    public  PageResult(int totalPage, List<T> resultList) {
        this.totalPage = totalPage;
        this.resultList = resultList;
    }

    //总页数
    private int totalPage;
    //结果集
    private List<T> resultList;


    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}
