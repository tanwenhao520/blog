package com.tan.blog.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tan.blog.pojo.Contents;
import com.tan.blog.pojo.DayWord;
import com.tan.blog.service.ContentService;
import com.tan.blog.service.DayWordServcie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author:TWH
 * @Date:2021/4/25 22:45
 */
@Api(description = "首页接口")
@RequestMapping("/index")
@RestController
public class IndexController {


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DayWordServcie dayWordServcie;

    @Autowired
    ContentService contentService;

    @ApiOperation( value = "获取首页图片",notes = "页面加载时获取首页轮播图片")
    @GetMapping("/indexImg")
    public List findIndexImg(){
        List img = redisTemplate.boundHashOps("img").values();
        return img;
    }

    @ApiOperation(value = "获取首页新闻",notes = "页面加载时获取新闻")
    @GetMapping("/findRecommend")
    public List<Map<String, String>> findRecommend(){
        Random random = new Random();
        BoundHashOperations dayRecommend = redisTemplate.boundHashOps("DayRecommend");
        int i = random.nextInt(2);
        Map<String,Map<String ,String>> map = new HashMap<>();
        System.out.println(i);
        List<Map<String, String>> map1 = (List<Map<String,String>>) dayRecommend.get(String.valueOf(i));
        return map1;
    }

    @ApiOperation(value = "每日一句",notes ="页面加载每日一句" )
    @GetMapping("/findDayWord")
    public DayWord dayWord(){
        DayWord dayWord = (DayWord)redisTemplate.boundHashOps("DayWord").get("0");
        if(dayWord != null){
            return dayWord;
        }else{
            List<DayWord> all = dayWordServcie.findAll();
            System.out.println(all.size());
            Random random = new Random();
            System.out.println(random.nextInt(all.size()));
            int i = random.nextInt(all.size())+1;
            DayWord one = dayWordServcie.findOne(i+"");
            return one;
        }
    }

    @ApiOperation(value = "热门文章",notes = "首页加载查询热门文章")
    @GetMapping("/findHotContent")
    public List<Contents> findHotContent(){
        if(!redisTemplate.hasKey("HotContent")){
            List<Contents> hotContent1 = contentService.findHotContent();
            for (Contents contents : hotContent1) {
                redisTemplate.boundHashOps("HotContent").put(contents.getId(),contents);
            }
        }
        List<Contents> list = (List<Contents>)redisTemplate.boundHashOps("HotContent").values();
        return list;
    }
}
