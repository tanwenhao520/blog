package com.tan.blog.dao;

import com.tan.blog.pojo.Contents;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ContentMapper extends Mapper<Contents> {

}
