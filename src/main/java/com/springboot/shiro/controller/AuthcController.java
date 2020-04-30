package com.springboot.shiro.controller;

import com.springboot.shiro.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authc")
public class AuthcController {

    @GetMapping("/video_watch")
    public Result watchVideo(){
        Map<String, Object> info = new HashMap<>();
        info.put("Springboot+Vue","前后端分离");
        info.put("SpringCloud","微服务开发");
        info.put("Mysql","数据库基础");
        return new Result(true,1,"授权成功！",info);
    }

}
