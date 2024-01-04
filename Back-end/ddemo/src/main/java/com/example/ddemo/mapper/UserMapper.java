package com.example.ddemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ddemo.entity.NewUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<NewUser> {

    @Select("select * from user order by birthday desc")
    List<NewUser> select_by_time();

    @Select("select * from user limit #{pageNum}, #{pageSize}")
    List<NewUser> findByPage(Integer pageNum, Integer pageSize);

    @Select("select count(*) from user")
    Integer userCount();

    @Select("SELECT * FROM user WHERE username = #{username} AND password = #{password}")
    NewUser findUserByUsernameAndPassword(String username, String password);
}
