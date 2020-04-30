package com.springboot.shiro.service;

import com.springboot.shiro.mapper.RoleMapper;
import com.springboot.shiro.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据用户id查询角色集合
     */
    public List<Role> findRolesByUserId(Long userId){
        return roleMapper.findRolesByUserId(userId);
    }

}
