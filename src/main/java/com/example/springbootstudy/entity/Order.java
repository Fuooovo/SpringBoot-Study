package com.example.springbootstudy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

@TableName("t_order")
public class Order {
    @TableId(type= IdType.AUTO)
    private int id;
    private String order_time;
    private String total;
    private int uid;

    @TableField(exist = false)
    private User user;


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", order_time='" + order_time + '\'' +
                ", total='" + total + '\'' +
                ", uid=" + uid +
                ", users=" + user +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
