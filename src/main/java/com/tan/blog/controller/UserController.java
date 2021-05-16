package com.tan.blog.controller;

import com.tan.blog.common.PageResult;
import com.tan.blog.common.Result;
import com.tan.blog.pojo.User;
import com.tan.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@Api(description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "注册用户名查重",notes = "注册时查询是否用户名重复")
    @GetMapping("/findRepeUserName")
    public  Result findUserName(@RequestParam String userName){
        User user = userService.findUserName(userName);
        if(user != null){
            return new Result(false,"用户名已存在!");
        }else {
            return new Result(true, "用户名可以使用!");
        }
    }

    @ApiOperation(value = "新增用户", notes = "新增用户")
    @PostMapping(value = "/addUser")
    public Result addUser(@RequestBody User user) {
        User newUser = userService.findUserName(user.getUserName());
        if(newUser != null){
            return Result.fail("注册失败!用户名已经存在,请重新输入!");
        }
        System.out.println(user.toString());
        int add = userService.addUser(user);
        if (add >= 1) {
            return Result.ok("注册成功");
        }
        return Result.fail("注册失败");
    }

    @ApiOperation(value = "修改用户", notes = "修改用户")
    @ApiImplicitParam(value = "用户的信息", paramType = "query")
    @PostMapping(value = "/updateUser")
    public Result updateUser(@RequestBody User user) {
        int update = userService.update(user);
        if (update >= 1) {
            return Result.ok("修改用户信息成功");
        }
        return Result.fail("修改用户信息失败");
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiImplicitParam(value = "用户的ID", paramType = "path")
    @DeleteMapping(value = "/deleteUser")
    public Result deleteUser(@RequestParam("userId") Serializable[] userIds) {
        boolean delete = userService.deleteByIds(userIds);
        if (delete) {
          return  Result.ok("删除成功");
        }
        return Result.fail("删除失败");
    }

    @ApiOperation(value = "查询用户", notes = "查询用户")
    @ApiImplicitParam(value = "获取用户列表", paramType = "query")
    @PostMapping(value = "/queryUser")
    public PageResult<User> queryUser(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "row", defaultValue = "10") Integer row, @RequestBody User user) {
        System.out.println(user.getCreateTime());
        return userService.findPage(page,row,user);

    }
}
