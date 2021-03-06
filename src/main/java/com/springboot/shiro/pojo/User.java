package com.springboot.shiro.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long userId;

    private String username;

    private String password;

    private Date createTime;

    private String salt;
    //账号状态，0表示锁定，1表示正常
    private String state;
    //角色集合
    private List<Role> roles;

}
