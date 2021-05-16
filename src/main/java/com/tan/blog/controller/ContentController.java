package com.tan.blog.controller;

import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.pojo.Contents;
import com.tan.blog.requsetBean.FindPageRequest;
import com.tan.blog.service.ContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@Api(description = "文章接口")
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    ContentService contentService;

    @ApiOperation(value = "查询文章", notes = "查询文章")
    @PostMapping("/findPage")
    public  PageResult<Contents> findPage(@RequestBody FindPageRequest findPageRequest) {
        System.out.println(findPageRequest.getContents());
        System.out.println(findPageRequest.getPage());
        PageResult<Contents> page = contentService.findPage(findPageRequest.getPage(), findPageRequest.getRow());
        return new PageResult<Contents>(((page.getTotalPage()+findPageRequest.getRow()-1)/ findPageRequest.getRow()),page.getResultList());
        //return contentService.findPage(page, row, contents);
    }

    @ApiOperation(value = "删除文章", notes = "按ids批量或者单个删除")
    @DeleteMapping("/deleteContent")
    public Result deleteByIds(@RequestParam(value = "ids") Serializable[] ids) {
        boolean b = contentService.deleteByIds(ids);
        if (b) {
            return Result.ok("删除成功！");
        }
        return Result.fail("删除失败!");
    }

    @ApiOperation(value = "添加文章", notes = "添加文章")
    @PostMapping("/addContent")
    public Result addContent(@RequestBody Contents contents) {
        int add = contentService.add(contents);
        if (add >= 1) {
            return Result.ok("添加成功！");
        }
        return Result.fail("添加失败！");
    }

    @ApiOperation(value = "修改文章", notes = "修改文章")
    @PostMapping("/updateContent")
    public Result updateContent(@RequestBody Contents contents) {
        int update = contentService.update(contents);
        if ( update >=1){
            return Result.ok("修改成功！");
        }
        return Result.fail("修改失败！");
    }

    @ApiOperation(value = "查询单个文章",notes ="查询单个文章" )
    @GetMapping("/findOneContent")
    public Contents findOne(@RequestParam(value = "id") String id){
        Contents one = contentService.findOne(id);
        return one;
    }

    @ApiOperation(value = "查询solr里面的文章并分页",notes = "查询solr里面的文章")
    @PostMapping("/search")
    public PageResult searchSolr(@RequestBody FindPageRequest findPageRequest){
        PageResult searchSolr = null;
        try {
            searchSolr = contentService.findSearchSolr(findPageRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchSolr;
    }

    @ApiOperation(value = "查询solr中的一个文章详细内容展示",notes ="查看Solr单个文章" )
    @GetMapping("/findContent")
    public Contents findContent(@RequestParam int id){
        return  contentService.findContent(id);
    }
}
