package com.example.springbootstudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudyController {

    // https://www.baidu.com/xx/xx  协议://域名/路径
    // localhost:8090/hello
    @GetMapping(value="/hello",produces = "application/json;charset=UTF-8")   //浏览器发送get请求访问hello方法
    public String hello(){
        return "你好";
    }
}
