package com.springboot.shiro.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;
/**
 * shiro中角色过滤器和权限过滤器的值有多个时，主体必须满足所以的值才能通过校验
 * 比如：/admin/order=roles["admin","root"],从角色过滤器的源码可以看到
 * 登录主体表示必须同时满足两个角色才能验证通过，这显然不符合实际情况
 * 因此需要自定义过滤器来改变过滤规则(拥有任意角色即可通过验证)
 * 下面是自定义的角色过滤器，重写了shiro默认的规则
 */
public class CustomRolesFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        //获取当前访问路径所需要的角色集合
        String[] roleArray = (String[]) mappedValue;
        if(roleArray == null || roleArray.length == 0){
            //数组中没有值，代表访问没有设置权限，可以放行
            return true;
        }
        Set<String> roles = CollectionUtils.asSet(roleArray);
        for (String role : roles) {
            //遍历角色集合，登陆主体中包含任意角色，即可放行
            subject.hasRole(role);
            return true;
        }
        return false;
    }
}
