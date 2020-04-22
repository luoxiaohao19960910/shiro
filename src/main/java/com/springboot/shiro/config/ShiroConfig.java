package com.springboot.shiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置securityManager,将它作为方法参数自动注入进来，前提是容器中已经有securityManager这个bean
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //访问的接口需要登录，但是还没登录时，应该调用此接口
        shiroFilterFactoryBean.setLoginUrl("/public/login");
        //登录成功，跳转的url
        shiroFilterFactoryBean.setSuccessUrl("/");
        //授权不通过跳转的url
        shiroFilterFactoryBean.setUnauthorizedUrl("/public/refuse");

        //将自定义的角色过滤器配置进来，与shiroFilterFactoryBean绑定
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        //key就是过滤器名称
        filterMap.put("rolesFilter",new CustomRolesFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //过滤路径的顺序是自上而下(有序)，所以不能使用HashMap
        Map<String, String> map = new LinkedHashMap<>();
        //过滤路径的配置，key为匹配的过滤路径，value为过滤器的名称
        //退出登录
        map.put("/logout","logout");
        //匿名访问
        map.put("/public/**","anon");
        //登录访问
        map.put("/user/**","authc");
        //角色访问(shiro默认的角色过滤器规则是必须满足所有角色才能放行)
        //map.put("/admin/**","roles[admin,root]");
        //角色过滤器规则被上面重写了，所以满足任意角色即可放行
        map.put("/admin/**","rolesFilter[admin,root]");
        //权限访问
        map.put("/video/update","perms[video:update]");
        //其余的所有路径需要登录访问
        map.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    /**
     * 构建SecurityManager环境
     */
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //使用自定义的sessionManager
        defaultWebSecurityManager.setSessionManager(sessionManager());
        //使用自定义的cacheManager
        defaultWebSecurityManager.setCacheManager(redisCacheManager());
        //使用自定义的realm，建议设置到最后面
        defaultWebSecurityManager.setRealm(customRealm());
        return defaultWebSecurityManager;
    }

    /**
     * 自定义Realm
     */
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

    /**
     * 加密处理
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //设置MD5散列算法
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        //散列的次数，等价于对MD5的结果再进行MD5加密
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义SessionManager
     */
    @Bean
    public SessionManager sessionManager(){
        CustomSessionManager customSessionManager = new CustomSessionManager();
        //设置超时时间，单位是毫秒，默认为30分钟过期
        //customSessionManager.setGlobalSessionTimeout(60000);
        //配置session持久化
        customSessionManager.setSessionDAO(redisSessionDAO());
        return customSessionManager;
    }

    /**
     * 配置redisManager
     */
    //@Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1:6379");
        return redisManager;
    }

    /**
     * 配置具体cache实现类
     */
    @Bean
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //设置缓存过期的时间，单位是秒，默认永不过期
        redisCacheManager.setExpire(30);
        return redisCacheManager;
    }

    /**
     * 配置具体cache实现类
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        //使用自定义的sessionId格式
        redisSessionDAO.setSessionIdGenerator(new CustomSessionIdGenerator());
        return redisSessionDAO;
    }

    /**
     * 管理shiro一些bean的生命周期 即bean的初始化与销毁
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 加入注解的使用，不加这个bean会导致controller中使用shiro的注解不生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 用来扫描上下文寻找所有的Advistor(通知器),将符合条件的Advistor应用到切入点的Bean中
     * 需要在LifecycleBeanPostProcessor创建后才可以创建
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(false);
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

}
