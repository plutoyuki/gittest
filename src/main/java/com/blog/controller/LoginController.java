package com.blog.controller;

import com.blog.common.lang.Result;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.util.JWTUtils;
import com.blog.vo.ErrorCode;
import com.blog.vo.param.LoginParam;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        if(StringUtils.isBlank(username)|| StringUtils.isBlank(password)){
            //用户名或密码为空
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //Post登陆请求
        User user = userMapper.getUser(username, password);
        if(user==null){
            //用户名或密码错误
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //创建并返回token
        Integer userid=user.getId();
        String token = JWTUtils.createToken(userid);
        redisTemplate.opsForValue().set("TOKEN_"+token,userid.toString(),1, TimeUnit.DAYS);
        return Result.succ(token);
    }
}
