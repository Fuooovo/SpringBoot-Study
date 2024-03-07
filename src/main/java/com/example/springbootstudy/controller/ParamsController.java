package com.example.springbootstudy.controller;

import com.example.springbootstudy.entity.User;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParamsController {

    @RequestMapping(value = "/getTest1",method = RequestMethod.GET)
    //@RequestParam注解可以映射参数名字
    public String getTest1(@RequestParam(value="nickname",required = false) String name){
        System.out.println("nickname:"+name);
        return "Get请求1";
    }

    @RequestMapping(value = "/postTest1",method = RequestMethod.POST)
    public String postTest1(){
        return "Post请求1";
    }

    @RequestMapping(value = "/postTest2",method = RequestMethod.POST)
    public String postTest2(String username,String password){
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        return "Post请求2";
    }

    @RequestMapping(value = "/postTest3",method = RequestMethod.POST)
    public String postTest3(User user){
        System.out.println(user.getUsername()+" "+user.getPassword());
        return "Post请求3";
    }

    @RequestMapping(value = "/postTest4",method = RequestMethod.POST)
    //@requestbody接收前端传给后端的json数据
    public String postTest4(@RequestBody User user){
        System.out.println(user.getUsername()+" "+user.getPassword());
        return "Post请求4";
    }

}
