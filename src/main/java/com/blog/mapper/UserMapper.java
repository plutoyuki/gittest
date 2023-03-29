package com.blog.mapper;

import com.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where id=#{id}")
    public User getById(Integer id);

    @Select("select * from user where username=#{username}")
    public User getByName(String username);

    @Select("select * from user where username=#{username} and password = #{password}")
    public User getUser(String username,String password);

    @Select("select * from user where username=#{username}")
    public User getByToken(String token);

    @Select("select username from user")
    public List<String> index();
}
