package com.tan.blog.task;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:TWH
 * @Date:2021/5/1 15:30
 */

//每日推荐模块
@Component
public class DayRecommend {

    //
    @Autowired
    RedisTemplate redisTemplate;
    //腾讯新闻
    public static final String TURL="https://xw.qq.com/";

    //其他热点 后面补充暂时爬腾讯新闻
    //public static final String URL="";
    //public static final String URL="";

    //每10分钟定时爬取新闻
    @Scheduled(cron = "0 10 * * * ?")
    public void httpByNew()throws Exception{
        int i = 0;
        redisTemplate.delete("DayRecommend");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(TURL);
        CloseableHttpResponse execute = httpClient.execute(httpGet);
        HttpEntity entity = execute.getEntity();
        String html = EntityUtils.toString(entity, "UTF-8");
        List list = parseId(html);
        System.out.println(list.size()+"-------------------------------------------");
        for (Object o : list) {
            i++;
            HttpGet httpGet1 = new HttpGet("https://xw.qq.com/cmsid/" + o.toString());
            System.out.println("https://xw.qq.com/cmsid/"+o.toString());
            CloseableHttpResponse response = httpClient.execute(httpGet1);
            HttpEntity entity1 = response.getEntity();
            String html1 = EntityUtils.toString(entity1, "UTF-8");
            Map<String,Map<String,String>> stringHashMapMap= parseContent(o.toString(),html1);
            System.out.println(stringHashMapMap.values());
            redisTemplate.boundHashOps("DayRecommend").put(String.valueOf(i),stringHashMapMap);
        }

    }

    //获取文章ID
    public List parseId(String html){
        List<String> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("container");
        System.out.println(elements.html());
        for (Element element : elements) {
            Elements a = element.select("a");
            for (Element element1 : a) {
                String href = element1.attr("href");
                list.add(href);
            }
        }
        return list;
    }

    //获取文章内容
    public Map<String,Map<String,String>> parseContent(String o,String html){
        Document document = Jsoup.parse(html);
        Elements title = document.getElementsByClass("title");
        Elements text = document.getElementsByClass("text");
        String content = text.text();
        Map map = new HashMap<String,Map<String,String >>();
        Map<String ,String> map1 = new HashMap<>();
        map1.put(title.text(),content);
        map.put(o,map1);
        return map;
    }


}
