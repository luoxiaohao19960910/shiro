package com.springboot.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springboot.shiro.mapper.UserMapper;
import com.springboot.shiro.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户基本信息
     */
    public User findUserByUsername(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username).select("password");
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    /**
     * 根据用户id查询用户基本信息
     */
    public User findUserByUserId(Long userId){
        return userMapper.selectById(userId);
    }

    /**
     * 根据用户名和密码查询用户基本信息
     */
    public User findUserByUsernameAndPassword(String username,String password){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username).eq("password",password);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 根据用户名查询用户详细信息(包括角色和权限)
     */
    public User findUserInfoByUsername(String username){
        User user = userMapper.findUserInfoByUsername(username);
        return user;
    }

}
