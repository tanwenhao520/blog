package com.tan.blog.service;

import com.tan.blog.common.PageResult;

import java.io.Serializable;
import java.util.List;

public interface  BaseService<T>  {
        //查询所有
        public List<T> findAll();
        //只查一条
        public T findOne(Serializable id);
        //按条件查
        public List<T> findWhere(T t);
        //查询并分页
        public PageResult<T> findPage(Integer page, Integer row);
        //添加
        public int add(T t);
        //更新
        public int update(T t);
        //批量/只按id删除
        public boolean deleteByIds(Serializable[] t);
}
