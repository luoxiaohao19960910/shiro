package com.springboot.shiro.config;

import com.springboot.shiro.pojo.Permission;
import com.springboot.shiro.pojo.Role;
import com.springboot.shiro.pojo.User;
import com.springboot.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     *  授权过程(权限校验时会调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("开始授权**********");
        //获取主身份信息
        User u = (User) principals.getPrimaryPrincipal();
        //根据用户名到数据库中查询用户详细信息(包括角色和权限)
        //String username = (String) principals.getPrimaryPrincipal();
        User user = userService.findUserInfoByUsername(u.getUsername());

        Set<String> stringRoles = new HashSet<>();
        Set<String> stringPermissions = new HashSet<>();
        //遍历用户的角色和权限，分别添加到对应的set集合中
        List<Role> roles = user.getRoles();
        for (Role role : roles) {
            stringRoles.add(role.getRoleName());
            List<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                stringPermissions.add(permission.getPermissionName());
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(stringRoles);
        simpleAuthorizationInfo.setStringPermissions(stringPermissions);
        return simpleAuthorizationInfo;
    }


    /**
     * 认证过程(用户登录时会调用)
     * UsernamePasswordToken类实现HostAuthenticationToken接口
     * 该接口又去继承了AuthenticationToken接口
     * 即方法参数token本质上就是UsernamePasswordToken对象(用户输入的账号和密码)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("开始认证**********");
        //从token中获取用户名
        //String username = (String)token.getPrincipal();
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        //根据用户名到数据库中查询用户基本信息
        User user = userService.findUserInfoByUsername(usernamePasswordToken.getUsername());
        //获取数据库中用户名对应的密码
        String password = user.getPassword();
        //对查询到的密码进行判断
        if(password == null || "".equals(password)){
           throw new UnknownAccountException("用户名或密码错误！");
        }
        if ("0".equals(user.getState())) {
            throw new LockedAccountException("账号已被锁定,请联系管理员！");
        }
        //构建认证结果信息，第一个参数要使用user，不能使用name,否则认证信息序列化到redis后会出现找不到主键的错误
        //第一个参数使用user后redis会自动去识别user对象的主键id作为主键标识
        return new SimpleAuthenticationInfo(user, password,new MyByteSource(user.getUsername()),this.getName());
    }
}
