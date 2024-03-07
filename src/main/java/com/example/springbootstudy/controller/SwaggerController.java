package com.example.springbootstudy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Api(tags = {"测试接口"})
public class SwaggerController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation(value = "测试swagger get")
    public String SwaggerGet(){
        return "swagger get!";
    }

}
