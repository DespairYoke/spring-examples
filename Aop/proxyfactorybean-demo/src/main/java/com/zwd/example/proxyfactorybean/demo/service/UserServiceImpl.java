package com.zwd.example.proxyfactorybean.demo.service;

import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContextAware;

/**
 * @author zwd
 * @date 2018/10/10 10:09
 * @Email stephen.zwd@gmail.com
 */
public class UserServiceImpl implements UserService{

    public void getUserid(String userId) {
        System.out.println("执行getUserid");
    }
}
