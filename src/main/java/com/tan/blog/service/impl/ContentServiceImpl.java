package com.tan.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.pojo.Contents;
import com.tan.blog.pojo.User;
import com.tan.blog.requsetBean.FindPageRequest;
import com.tan.blog.service.ContentService;
import lombok.Data;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ContentServiceImpl extends BaseServiceImpl<Contents> implements ContentService {

    @Autowired
    HttpSolrClient httpSolrClient;


    //按条件增加文章，创建时间为当前时间,修改时间为Null，浏览量为0，点赞数为0
    public int add(Contents contents) {
        contents.setPraise("0");
        contents.setPv("0");
        contents.setCreateTime(new Date());
        return mapper.insert(contents);
    }

    //按条件修改文章，第一次新增修改时间，第二次修改修改时间
    public int update(Contents contents) {
        contents.setUpdateTime(new Date());
        return mapper.updateByPrimaryKeySelective(contents);
    }

    @Override
    public PageResult<Contents> findPage(FindPageRequest findPageRequest) {
        List<Contents> list = new ArrayList<Contents>();
        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(Integer.valueOf(findPageRequest.getPage()), Integer.valueOf(findPageRequest.getRow().toString()));
        Example example = new Example(Contents.class);
        if (!("".equals(findPageRequest.getContents().getTitle())) || (Objects.isNull(findPageRequest.getContents().getTitle()))) {
            example.createCriteria().andLike("title", "%" + findPageRequest.getContents().getTitle() + "%");
        }
        if (!("".equals(findPageRequest.getContents().getContent())) || (Objects.isNull(findPageRequest.getContents().getContent()))) {
            example.createCriteria().andLike("content", "%" + findPageRequest.getContents().getContent() + "%");
        }
        if (!("".equals(findPageRequest.getContents().getIssuer())) || (Objects.isNull(findPageRequest.getContents().getIssuer()))) {
            example.createCriteria().andLike("issuer", "%" + findPageRequest.getContents().getIssuer() + "%");
        }
        if (!("".equals(findPageRequest.getContents().getTag())) || (Objects.isNull(findPageRequest.getContents().getTag()))) {
            example.createCriteria().andLike("tag", "%" + findPageRequest.getContents().getTag() + "%");
        }
        if (!("".equals(findPageRequest.getContents().getId())) || (Objects.isNull(findPageRequest.getContents().getId()))) {
            example.createCriteria().andLike("id", "%" + findPageRequest.getContents().getId() + "%");
        }

        if (findPageRequest.getContents().getTitle().equals("") && findPageRequest.getContents().getTitle() == null
                && findPageRequest.getContents().getContent().equals("") && findPageRequest.getContents().getContent() == null
                && findPageRequest.getContents().getTag().equals("") && findPageRequest.getContents().getTag() == null
                && findPageRequest.getContents().getId().equals("") && findPageRequest.getContents().getId() == null
                && findPageRequest.getContents().getIssuer().equals("") && findPageRequest.getContents().getIssuer() == null) {
            list = mapper.selectAll();
        } else {
            list = mapper.selectByExample(example);
        }
        PageInfo pageInfo = new PageInfo(list);
        return new PageResult((int)pageInfo.getTotal(), pageInfo.getList());
    }

    //查找solr数据
    @Override
    public PageResult findSearchSolr(FindPageRequest findPageRequest) throws Exception {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setStart((findPageRequest.getPage()-1)* (findPageRequest.getRow()));
        solrQuery.setRows(findPageRequest.getRow());
        System.out.println(  URLDecoder.decode(findPageRequest.getContents().getTitle(),"UTF-8"));

        String title = URLDecoder.decode(findPageRequest.getContents().getTitle(),"utf-8");
        if(!StringUtils.isEmpty(findPageRequest.getContents().getTitle())){
            //设置Solr查询条件
            solrQuery.set("q","item_title:"+ title);
            solrQuery.setHighlight(true);
            solrQuery.addHighlightField("item_title");
            solrQuery.setHighlightSimplePre("<font color='red'>");
            solrQuery.setHighlightSimplePost("</font>");
            QueryResponse query = httpSolrClient.query(solrQuery);
            SolrDocumentList results = query.getResults();
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
            List<Object> list = new ArrayList<>();
            for (SolrDocument result : results) {
                List<String> strings = highlighting.get(result.get("item_id").toString()).get("item_title");
                String s = strings.get(0);
                System.out.println(s);
                Contents contents = new Contents();
                contents.setId(result.get("item_id").toString());
                contents.setTitle(s);
                contents.setContent(result.get("item_content").toString());
                if(!StringUtils.isEmpty(result.get("item_createtime").toString())){
                    contents.setCreateTime((Date)(result.get("item_createtime")));
                }
                if(!StringUtils.isEmpty(result.get("item_updatetime").toString())){
                    contents.setUpdateTime((Date)(result.get("item_updatetime")));
                }
                contents.setIssuer(result.get("item_issuer").toString());
                contents.setPraise(result.get("item_praise").toString());
                contents.setPv(result.get("item_pv").toString());
                contents.setTitleImg(result.get("item_titleimg").toString());
                contents.setTitle(s);
                contents.setTag(result.get("item_tag").toString());
                list.add(contents);
            }
            return new PageResult(((int)results.getNumFound()+findPageRequest.getRow()-1)/findPageRequest.getRow(),list);
        }else{
            solrQuery.set("q","item_title:*");
            QueryResponse query = httpSolrClient.query(solrQuery);
            SolrDocumentList results = query.getResults();
            List<Object> list = new ArrayList<>();
            for (SolrDocument result : results) {
                Contents contents = new Contents();
                contents.setId(result.get("item_id").toString());
                contents.setTitle(result.get("item_title").toString());
                if(!StringUtils.isEmpty(result.get("item_createtime").toString())){
                    contents.setCreateTime((Date) result.get("item_createtime"));
                }
                if(!StringUtils.isEmpty(result.get("item_updatetime").toString())){
                    contents.setUpdateTime((Date) result.get("item_updatetime"));
                }
                contents.setIssuer(result.get("item_issuer").toString());
                contents.setPraise(result.get("item_praise").toString());
                contents.setPv(result.get("item_pv").toString());
                contents.setTitleImg(result.get("item_titleimg").toString());
                contents.setTitle(result.get("item_title").toString());
                contents.setTag(result.get("item_tag").toString());
                list.add(contents);
            }
            System.out.println(results.getNumFound());
            return new PageResult(((int)results.getNumFound()+findPageRequest.getRow()-1)/findPageRequest.getRow(),list);
        }
    }
    //查询单个文章按ID查，当用户点击文章标题时按ID查看当前文章的详细内容。
    @Override
    public Contents findContent(int id) {
        List<Contents> list = new ArrayList<>();
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","item_id:"+id);
        try {
            QueryResponse query = httpSolrClient.query(solrQuery);
            SolrDocumentList results = query.getResults();
            SolrDocument result = results.get(0);
            Contents contents = new Contents();
            contents.setId(result.get("item_id").toString());
            contents.setTag(result.get("item_tag").toString());
            contents.setTitle(result.get("item_title").toString());
            contents.setTitleImg(result.get("item_titleimg").toString());
            contents.setPv(result.get("item_pv").toString());
            contents.setPraise(result.get("item_praise").toString());
            contents.setIssuer(result.get("item_issuer").toString());
            contents.setCreateTime((Date) result.get("item_createtime"));
            contents.setUpdateTime((Date)result.get("item_updatetime"));
            contents.setContent(result.get("item_content").toString());
            return contents;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
    //随机查找5个热门文章加载
    @Override
    public List<Contents> findHotContent() {
        Random random = new Random();
        List<Contents> contents = mapper.selectAll();
        int i = random.nextInt(contents.size() - 4);
        PageResult<Contents> page = findPage((i/5), 5);
        return page.getResultList();
    }


}
