package com.tan.blog.controller;

import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.pojo.DayWord;
import com.tan.blog.service.DayWordServcie;
import io.swagger.annotations.ApiOperation;
import org.apache.solr.client.solrj.request.V1toV2ApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:TWH
 * @Date:2021/5/3 23:25
 */
@RequestMapping("/DayWord")
@RestController
public class DayWordController {

    @Autowired
    DayWordServcie dayWordServcie;

    @Autowired
    RedisTemplate redisTemplate;

    @ApiOperation(value = "添加每日一句",notes = "添加每日一句")
    @PostMapping("/addDayWord")
    public Result addDayWord(@RequestParam  String dayWord){
        DayWord dayWord1 = new DayWord();
        dayWord1.setWord(dayWord);
        int add = dayWordServcie.add(dayWord1);
        if(add > 0 ){
            return  new Result(true,"添加成功！");
        }
        return new Result(false,"添加失败！");
    }

    @ApiOperation(value = "删除每日一句",notes = "删除每日一句")
    @PostMapping("/deleteDayWord")
    public Result deleteDayWord(Serializable[] ids){
        if(ids.length > 0){
            boolean b = dayWordServcie.deleteByIds(ids);
            if(b){
                return new Result(true,"删除成功！");
            }else{
                return new Result(false,"删除失败！");
            }
        }else{
            return new Result(false,"删除失败！");
        }
    }

    @ApiOperation(value = "查询每日一句",notes ="查询每日一句")
    @GetMapping("/selectDayWord")
    public List<DayWord> findDayWord(){
        List<DayWord> all = dayWordServcie.findAll();
        return all;
    }

/*    @ApiOperation(value = "查询Redis中的每日一句")
    @GetMapping("/indexSelectDayWord")
    public DayWord indexFindDayWord(){
        DayWord dayWord = (DayWord) redisTemplate.boundHashOps("dayWord").get("0");
        if(dayWord != null){
            return  dayWord;
        }
        return null;
    }*/

}
