package com.tan.blog.config;

import com.tan.blog.pojo.DayWord;
import com.tan.blog.service.DayWordServcie;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * @Author:TWH
 * @Date:2021/4/26 21:34
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext.xml")
public class SpringTestToRedis {

    @Autowired
    RedisTemplate redisTemplate;
    //添加图片到Redis Hash

    @Autowired
    DayWordServcie dayWordServcie;
    private List<Map<String, String>> list;

    @Test
    public void addImg(){
        redisTemplate.boundHashOps("img").put("1","http://shp.qpic.cn/ishow/2735042018/1618915965_84828260_2160_sProdImgNo_2.jpg/0");
        redisTemplate.boundHashOps("img").put("2","http://shp.qpic.cn/ishow/2735041519/1618485629_84828260_22420_sProdImgNo_2.jpg/0");
        redisTemplate.boundHashOps("img").put("3","http://shp.qpic.cn/ishow/2735010717/1610011288_84828260_7033_sProdImgNo_2.jpg/0");
    }
    //获取Redis存放的图片
    @Test
    public void getImg(){
        List img = redisTemplate.boundHashOps("img").values();
        for (Object o : img) {
            System.out.println(o.toString());
        }
        
    }


    //
    @Test
    public  void addStringImg(){
        redisTemplate.boundValueOps("img2").set("我罗哥天下无敌");
    }



    //腾讯新闻
    public static final String TURL="https://xw.qq.com/";

    //其他热点 后面补充暂时爬腾讯新闻
    //public static final String URL="";
    //public static final String URL="";

    //每天定时爬取腾讯新闻并加入到Redis数据库
   @Test
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
            HttpGet httpGet1 = new HttpGet("https://xw.qq.com/cmsid/" + o.toString());
            System.out.println("https://xw.qq.com/cmsid/"+o.toString());
            CloseableHttpResponse response = httpClient.execute(httpGet1);
            HttpEntity entity1 = response.getEntity();
            String html1 = EntityUtils.toString(entity1, "UTF-8");
            List<Map<String, String>> stringStringMap = parseContent(o.toString(),html1);
            redisTemplate.boundHashOps("DayRecommend").put(String.valueOf(i),stringStringMap);
            i++;
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
    public List<Map<String,String>> parseContent(String o,String html){
        Document document = Jsoup.parse(html);
        Elements title = document.getElementsByClass("title");
        Elements text = document.getElementsByClass("text");
        String content = text.html();
        Map map = new HashMap<String,Map<String,List<Map<String,String>>>>();
        List<Map<String,String>> ist = new ArrayList<>();
        Map<String,String> map1 = new HashMap();
        map1.put("id",o.toString());
        map1.put("title",title.text());
        map1.put("content",text.text());
        list.add(map1);
        return list;
    }

    //添加每日一句到Redis
    @Test
    public void taskDayWordToRedis(){
       if(redisTemplate.boundHashOps("DayWord").keys().size() > 0 )
        redisTemplate.boundHashOps("DayWord").delete("0");
        List<DayWord> all = dayWordServcie.findAll();
        System.out.println(all.size());
        Random random = new Random();
        System.out.println(random.nextInt(all.size()));
        int i = random.nextInt(all.size())+1;
        DayWord one = dayWordServcie.findOne(i);
        redisTemplate.boundHashOps("DayWord").put("0",one);
    }
}
