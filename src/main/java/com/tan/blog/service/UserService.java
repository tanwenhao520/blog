package com.tan.blog.service;

import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.pojo.User;


public interface UserService extends BaseService<User>{

    public PageResult<User> findPage(Integer page, Integer row, User user);

    public User findCookieId(String id);

    public int addUser(User user);

    public User findUserName(String username);
}
