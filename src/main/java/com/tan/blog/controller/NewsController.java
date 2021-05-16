package com.tan.blog.controller;

import com.tan.blog.common.Result;
import com.tan.blog.pojo.News;
import com.tan.blog.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@Api(description = "公告接口")
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService newsService;

    @ApiOperation(value = "查询所有公告",notes = "查询所有公告")
    @GetMapping("/findAll")
    public List<News> findAll(){
        return newsService.findAll();
    }

    @ApiOperation(value = "新增公告",notes = "新增公告")
    @PostMapping("/addNews")
    public Result addNews(@RequestBody News news){
        int add = newsService.add(news);
        if(add >= 1){
          return  Result.ok("新增成功！");
        }
        return Result.fail("新增失败！");
    }

    @ApiOperation(value = "删除公告",notes = "删除公告")
    @DeleteMapping("/deleteNews")
    public Result deleteNews(@RequestParam(value = "ids")Serializable[] ids){
        boolean b = newsService.deleteByIds(ids);
        if(b){
          return   Result.ok("删除成功！");
        }
        return Result.fail("删除失败！");
    }
}
