package com.springboot.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springboot.shiro.pojo.Role;
import com.springboot.shiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色集合
     */
    List<Role> findRolesByUserId(Long userId);

}