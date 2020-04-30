package com.springboot.shiro.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long roleId;

    private String roleName;

    private String description;
    //权限集合
    private List<Permission> permissions;

}
