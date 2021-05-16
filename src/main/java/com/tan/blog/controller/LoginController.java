package com.tan.blog.controller;

import com.tan.blog.common.CookieUtil;
import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.pojo.User;
import com.tan.blog.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author:TWH
 * @Date:2021/4/5 22:15
 */
@Api(description = "登录接口")
@RestController
@RequestMapping("login/")
public class LoginController {

    @Autowired
    UserServiceImpl userService;

    public static final String cookieName= "USER_CS";

    /**
     * 用户登录
     * @param request
     * @param response
     * @param userName
     * @param passWord
     * @return
     */
    @ApiOperation(value = "登录账户",notes = "登录账户")
    @GetMapping("/loginUser")
    public Result loginUser(HttpServletRequest request, HttpServletResponse response, @RequestParam String userName, @RequestParam String passWord){
        User user = new User();
        user.setUserName(userName);
        user.setPassWord(passWord);
        List<User> where = userService.findWhere(user);
        try {
            if(where.size() > 0){
                String id = where.get(0).getId();
                String userName1 = where.get(0).getUserName();
                String passWord1 = where.get(0).getPassWord();
                StringBuffer cookieValue = new StringBuffer(id+"|"+userName1+"|"+passWord1);
                CookieUtil.setCookie(request,response,cookieName,cookieValue.toString());
                System.out.println(cookieValue);
                return  new Result(true,"登录成功");
            }else {
                return  new Result(false,"用户名或者密码不正确，请重新输入！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"服务器错误");

        }
    }

    @ApiOperation(value = "查看是否登录页面是否有Cookie",notes = "查看登录Cookie")
    @GetMapping("/findLoginCookie")
    public User findLoginCookie(HttpServletRequest request,HttpServletResponse response){
        if(CookieUtil.getCookie(request, cookieName) != null){
            String cookie = CookieUtil.getCookie(request, cookieName);
            if(cookie != "" && cookie != null){
                String[] split = cookie.split("\\|");
                return  userService.findCookieId(split[0]);
            }
        }

        return null;
    }
    @ApiOperation(value = "退出登录",notes = "退出登录删除Cookie")
    @GetMapping("/qiut")
    public Result qiut(HttpServletRequest request,HttpServletResponse response){
        try {
            CookieUtil.delCookie(request,response,cookieName);
            return Result.ok("退出成功");
        } catch (Exception e) {
           return Result.fail("退出失败");
        }
    }


}
