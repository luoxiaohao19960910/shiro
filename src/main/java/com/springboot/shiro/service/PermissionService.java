package com.springboot.shiro.service;

import com.springboot.shiro.mapper.PermissionMapper;
import com.springboot.shiro.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> findPermisssionsByRoleId(Long roleId){
        return permissionMapper.findPermisssionsByRoleId(roleId);
    }

}
