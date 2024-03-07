package com.example.springbootstudy.controller;

import com.example.springbootstudy.entity.Order;
import com.example.springbootstudy.entity.User;
import com.example.springbootstudy.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/order/findAll")
    public List<Order> findAll(){
        List<Order> orders = orderMapper.selectAllOrdersAndUsers();
        System.out.println(orders);
        return orders;
    }


}
