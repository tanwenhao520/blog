package com.tan.blog.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author:TWH
 * @Date:2021/5/1 14:58
 */

//测试SpringTask是否能输出
@Component
public class SpringTask {

    @Scheduled(cron = "0/10 * * * * ?")
    public void outFont(){
        System.out.println("定时任务");
    }
}
