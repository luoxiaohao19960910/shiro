package com.springboot.shiro.controller;

import com.springboot.shiro.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/perms")
public class PermsController {

    @GetMapping("/order/watch")
    public Result watchOrder(){
        return new Result(true,1,"具有权限，可以访问");
    }
}
