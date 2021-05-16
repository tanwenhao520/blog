package com.tan.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    Mapper<T> mapper;

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public T findOne(Serializable id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findWhere(T t) {
        return mapper.select(t);
    }

    //查询并分页
    @Override
    public PageResult<T> findPage(Integer page, Integer row) {
        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(page, row);
        List<T> ts = mapper.selectAll();
        PageInfo<T> pageInfo = new PageInfo<T>(ts);
        return new PageResult((int)pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public int add(T t) {
        int insert = mapper.insert(t);
        return insert;
    }

    @Override
    public int update(T t) {
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public boolean deleteByIds(Serializable[] ids) {
        boolean flag = false;
        for (Serializable id : ids) {
            int i = mapper.deleteByPrimaryKey(id);
            System.out.println(i);
            if (i > 0) {
                flag = true;
            }
        }
        return flag;

    }

}
