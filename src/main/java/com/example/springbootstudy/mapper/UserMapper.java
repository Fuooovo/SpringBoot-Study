package com.example.springbootstudy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootstudy.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    //通过id查找用户信息
    @Select("select * from t_user where id = #{id}")
    User selectById(int id);

    //通过id查找用户订单，并返回用户信息和对应的订单信息
    @Select("select * from t_user")
    @Results(
            {       //column=字段名，property=属性名
                    //@Result注解把从数据库中查询到的数据，赋值给类的属性
                    @Result(column = "id",property = "id"),
                    @Result(column = "username",property = "username"),
                    @Result(column = "password",property = "password"),
                    @Result(column = "birthday",property = "birthday"),
                    //在userMapper中调用orderMapper的selectByUid方法，把id传递给该方法，得到的订单集合赋值给User类里的orders属性
                    @Result(column = "id",property = "orders",javaType = List.class,
                        many=@Many(select="com.example.springbootstudy.mapper.OrderMapper.selectByUid")
                    )
            }
    )
    List<User> selectAllUsersAndOrders();
}
