package com.tan.blog.task;

import com.tan.blog.pojo.DayWord;
import com.tan.blog.service.DayWordServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * @Author:TWH
 * @Date:2021/5/7 12:51
 */

//每天定时将每日一句添加到到Redis
@Component
public class DayWrodToRedis {

    @Autowired
    DayWordServcie dayWordServcie;

    @Autowired
    RedisTemplate redisTemplate;

    @Scheduled(cron = "0 0 0 * * ?")
    public void taskDayWordToRedis(){
        if(redisTemplate.boundHashOps("DayWord").keys().size() > 0){
            redisTemplate.boundHashOps("DayWord").delete();
        }
        List<DayWord> all = dayWordServcie.findAll();
        Random random = new Random();
        DayWord one = dayWordServcie.findOne(random.nextInt(all.size())+1);
        redisTemplate.boundHashOps("dayWord").put("0",one);
    }
}
