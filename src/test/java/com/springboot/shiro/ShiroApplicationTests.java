//package com.springboot.shiro;
//
//import com.springboot.shiro.config.CustomRealm;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.UsernamePasswordToken;
//import org.apache.shiro.crypto.hash.SimpleHash;
//import org.apache.shiro.mgt.DefaultSecurityManager;
//import org.apache.shiro.subject.Subject;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.expression.spel.ast.NullLiteral;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ShiroApplicationTests {
//
//    private CustomRealm customRealm = new CustomRealm();
//
//    private DefaultSecurityManager securityManager = new DefaultSecurityManager();
//
//    @Before
//    public void before() {
//        securityManager.setRealm(customRealm);
//        SecurityUtils.setSecurityManager(securityManager);
//    }
//
//    /**
//     * 测试认证过程
//     */
//    @Test
//    public void testAuthentication(){
//        //获取当前操作的主体(账号)
//        Subject subject = SecurityUtils.getSubject();
//        //用户名输入的账号密码
//        UsernamePasswordToken token = new UsernamePasswordToken("张三", "123");
//        //登录
//        subject.login(token);
//
//        System.out.println("认证结果：" + subject.isAuthenticated());
//
//        System.out.println("账号为：" + subject.getPrincipal());
//    }
//
//    @Test
//    public void testMD5(){
//        SimpleHash result = new SimpleHash("MD5", "123456", null, 2);
//        System.out.println(result);
//    }
//
//}
