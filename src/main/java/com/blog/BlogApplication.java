package com.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.blog.mapper"})
public class BlogApplication {
    public static void main(String[] args) {
//        try{
            SpringApplication.run(BlogApplication.class, args);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
