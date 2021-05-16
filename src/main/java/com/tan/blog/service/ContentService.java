package com.tan.blog.service;

import com.tan.blog.common.PageResult;
import com.tan.blog.pojo.Contents;
import com.tan.blog.requsetBean.FindPageRequest;

import java.util.List;

public interface ContentService extends BaseService<Contents> {

    public PageResult<Contents> findPage(FindPageRequest findPageRequest);

    PageResult findSearchSolr(FindPageRequest findPageRequest) throws Exception;

    Contents findContent(int id);

    //随机查找5篇文章
    List<Contents> findHotContent();
}
