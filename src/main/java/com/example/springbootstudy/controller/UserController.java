package com.example.springbootstudy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootstudy.entity.Order;
import com.example.springbootstudy.entity.User;
import com.example.springbootstudy.mapper.OrderMapper;
import com.example.springbootstudy.mapper.UserMapper;
import com.example.springbootstudy.utils.JwtUtils;
import com.example.springbootstudy.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    //querystring: username=zhangsan&password=123  User user,String username,String password
    //json: {username:zhangsan,password:123}
    //如果前端传递的数据是json格式，必须使用对象接收，同时需要添加@RequestBody注解
    public Result login(@RequestBody User user){
        String token= JwtUtils.generateToken(user.getUsername());
        return Result.ok().data("token",token);
    }


    @GetMapping("/info")
    public Result info(String token){
        String username= JwtUtils.getClaimsByToken(token).getSubject();
        String url="https://cdn.pixabay.com/photo/2016/03/31/19/23/cat-1294968_1280.png";
        return Result.ok().data("name",username).data("avator",url);
    }




    @GetMapping("/findAll")
    public List<User> findAll(){
        List<User> users = userMapper.selectAllUsersAndOrders();
        System.out.println(users);
        return users;
    }

    //mybatis-plus的条件查询
    @GetMapping("/findByCond")
    public List<User> findByCond(){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username","zhangsan");
        return userMapper.selectList(queryWrapper);
    }

    //mybatis-plus分页查询
    @GetMapping("/findByPage")
    public IPage findByPage(){
        //设置起始值以及每页条数
        Page<User> page = new Page<>(0,2);
        //iPage封装了查询的结果，以及当前是第几页等额外信息
        IPage iPage = userMapper.selectPage(page,null);
        return iPage;
    }



    @PostMapping
    public String addUser(User user){
        //前端传过来的数据插入到数据库中
        int i = userMapper.insert(user);
        if(i>0){
            return "插入成功";
        }
        return "插入失败";
    }

/*
    @GetMapping("/user/{id}")
    public  String getUserById(@PathVariable int id){
        System.out.println(id);
        return "根据ID获取用户信息";
    }

    @PostMapping("/user")
    public String save(User user){
        return "添加用户";
    }

    @PutMapping("/user")
    public String update(User user){
        return "更新用户";
    }

    @DeleteMapping("/user/{id}")
    public String deleteById(@PathVariable int id){
        System.out.println(id);
        return "根据ID删除用户";
    }
 */

}
