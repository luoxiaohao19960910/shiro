package com.springboot.shiro.controller;

import com.springboot.shiro.pojo.Result;
import com.springboot.shiro.pojo.UserQuery;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pub")
public class PubController {

    @GetMapping("/need_login")
    public Result needLogin(){
        return new Result(false,-1,"请先进行登录！");
    }

    @GetMapping("/refuse")
    public Result refuse(){
        return new Result(false,-2,"没有权限，拒绝访问！");
    }

    @GetMapping("/index")
    public Result index(){
        List<String> list = new ArrayList<>();
        list.add("1.你好");
        list.add("2.我好");
        list.add("3.大家好");
        return new Result(true,1,"成功访问首页内容！",list);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserQuery userQuery){
        Map<String,Object> info = new HashMap<>();
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userQuery.getUsername(),userQuery.getPassword());
            Subject subject = SecurityUtils.getSubject();
            subject.login(usernamePasswordToken);
            info.put("haohao",subject.getSession().getId());
            return new Result(true,1,"登录成功",info);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new Result(false,-1,"登录失败");
        }
    }

}
