package com.tan.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.pojo.User;
import com.tan.blog.service.UserService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
//查询用户并分页
    @Override
    public PageResult<User> findPage(Integer page, Integer row, User user) {
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        PageHelper pageHelper = new PageHelper();
        pageHelper.startPage(page, row);
        Example example = new Example(User.class);
        if (user.getId() != null && !user.getId().equals("")) {
            example.createCriteria().andLike("id", "%" + user.getId() + "%");
        }
        if (user.getUserName() != null && !user.getUserName().equals("")) {
            example.createCriteria().andLike("userName", "%" + user.getUserName() + "%");
        }
        if (user.getPhone() != null && !user.getPhone().equals("")) {
            example.createCriteria().andLike("phone", "%" + user.getPhone() + "%");
        }
        if (user.getCreateTime() != null && !user.getCreateTime().equals("")) {
            example.createCriteria().andLike("createTime", "%" + sdf.format(user.getCreateTime()) + "%");
        }
        if (user.getEmail() != null && !user.getEmail().equals("")) {
            example.createCriteria().andLike("email", "%" + user.getEmail() + "%");
        }
        if (user.getId()!= null && ! user.getId().equals("") || user.getUserName() != null && !user.getUserName().equals("") || user.getPhone() != null && !user.getPhone().equals("") && user.getCreateTime() != null && !user.getCreateTime().equals("")) {
            users = mapper.selectByExample(example);
        } else {
            users = mapper.selectAll();
        }
        PageInfo pageInfo = new PageInfo(users);
        return new PageResult<User>((int)pageInfo.getTotal(), pageInfo.getList());
    }
//查找Cookie中的用户
    @Override
    public User findCookieId(String id) {
        User user = new User();
        user.setId(id);
        return mapper.selectOne(user);
    }
//添加用户
    @Override
    public int addUser(User user) {
       user.setCreateTime(new Date());
        return mapper.insert(user);
    }
//查询用户名用于判断是否有重复的用户名
    @Override
    public User findUserName(String username) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("userName",username);
        List<User> users = mapper.selectByExample(example);
        if(users.size() > 0){
            return users.get(0);
        }
        return  null;
    }

}
