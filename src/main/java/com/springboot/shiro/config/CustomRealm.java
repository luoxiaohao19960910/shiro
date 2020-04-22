package com.springboot.shiro.config;

import com.springboot.shiro.pojo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义realm
 */
public class CustomRealm extends AuthorizingRealm {

    //授权过程
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("开始授权**********");
        //获取主身份信息
        User user = (User) principals.getPrimaryPrincipal();
        //模拟根据上面的主身份信息(用户名:user.getName())到数据库中查询对应的角色集合和权限集合
        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();
        roles.add("admin");
        roles.add("root");
        permissions.add("aaa");
        permissions.add("bbb");
        permissions.add("ccc");

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }


    /**
     * 认证过程
     * UsernamePasswordToken类实现HostAuthenticationToken接口
     * 该接口继承AuthenticationToken接口
     * 即方法参数token本质上就是用户输入的账号和密码
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("开始认证**********");
        //从token中获取用户的账号
        String name = (String)token.getPrincipal();
        //模拟根据上面的用户名到数据库中查询用户信息
        User user = new User(1L,"张三","123");
        String pwd = user.getPwd();
        //对查询到的结果进行判断
        if(pwd == null || "".equals(pwd)){
            return null;
        }
        //构建认证的结果信息
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, pwd, this.getName());

        return simpleAuthenticationInfo;
    }
}
