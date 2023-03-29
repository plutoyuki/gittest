package com.blog.controller;


import com.blog.common.lang.Result;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.util.JWTUtils;
import com.blog.vo.ErrorCode;
import freemarker.template.utility.StringUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate<String,String> redisTemplate;


    @PostMapping
    public Result currentUser(@RequestBody String token){
        //偷懒解决格式问题
        StringBuffer buffer = new StringBuffer(token);
        buffer.deleteCharAt(token.length() - 1);//删除最后位的元素
        token = buffer.toString();
        //校验token
        //System.out.println(token);
        if(StringUtils.isBlank(token)){return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());}
        if(JWTUtils.checkToken(token)==null){return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());}
        //查找用户
        Integer id = Integer.valueOf(
                redisTemplate.opsForValue().get("TOKEN_" + token)
        );
        if (StringUtils.isBlank(id.toString())){return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());}
        User user = userMapper.getById(id);
        //!!!这里把密码一起传过去了！！！
        return Result.succ(user);
    }

    @GetMapping("/index")
    public Result index(){
        List<String> users = userMapper.index();
        System.out.println(users);
        return Result.succ(200,"操作成功",users);
    }
}
