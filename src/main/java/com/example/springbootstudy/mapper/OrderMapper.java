package com.example.springbootstudy.mapper;


import com.example.springbootstudy.entity.Order;
import com.example.springbootstudy.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    //通过uid查找订单信息
    @Select("select * from t_order where uid = #{uid}")
    List<Order> selectByUid(int uid);

    //通过uid查找订单信息，并返回订单信息和对应的用户信息
    @Select("select * from t_order")
    @Results(
            {
                    @Result(column="id",property = "id"),
                    @Result(column="order_time",property = "order_time"),
                    @Result(column="total",property = "total"),
                    @Result(column="uid",property = "uid"),
                    @Result(column="uid",property = "user",javaType = User.class,
                        one=@One(select="com.example.springbootstudy.mapper.UserMapper.selectById")
                    )
            }
    )
    List<Order> selectAllOrdersAndUsers();
}
