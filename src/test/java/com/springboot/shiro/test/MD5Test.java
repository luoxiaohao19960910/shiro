package com.springboot.shiro.test;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class MD5Test {

    @Test
    public void testMD5(){
        String hashName = "MD5";
        String password = "333";
        String salt = "luoxiaohao";
        SimpleHash result = new SimpleHash(hashName, password, salt, 2);
        System.out.println(result);
    }
}
