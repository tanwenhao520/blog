package com.tan.blog.dao;

import com.tan.blog.pojo.News;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface NewMapper extends Mapper<News> {
}
