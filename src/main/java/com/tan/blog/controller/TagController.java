package com.tan.blog.controller;

import com.tan.blog.common.Result;
import com.tan.blog.pojo.Tag;
import com.tan.blog.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "标签管理接口")
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    TagService tagService;

    @ApiOperation(value = "查询标签", notes = "查询所有标签")
    @GetMapping("/findAll")
    public List<Tag> findAll() {
        return tagService.findAll();
    }

    @ApiOperation(value = "新增标签",notes = "新增标签")
    @PostMapping("/addTag")
    public Result addTag(@RequestBody Tag tag){
        int add = tagService.add(tag);
        if(add >= 1){
            return Result.ok("新增成功！");
        }
        return Result.fail("新增失败!");
    }

}
