package com.springboot.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.shiro.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据角色id查询权限集合
     */
    List<Permission> findPermisssionsByRoleId(Long roleId);

}
