package com.springboot.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.shiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户详细信息(包括角色和权限)
     */
    User findUserInfoByUsername(String username);
}
