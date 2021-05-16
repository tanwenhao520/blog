package com.tan.blog.service.impl;

import com.tan.blog.common.Result;
import com.tan.blog.pojo.News;
import com.tan.blog.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class NewsServiceImpl extends BaseServiceImpl<News> implements NewsService {


    public List<News> findAll() {
        return mapper.selectAll();
    }

    public int add(News news){
        news.setTime(new Date());
        return mapper.insert(news);
    }

}
