package com.zwd.example.test;

import java.lang.reflect.Proxy;

/**
 * @author zwd
 * @date 2018/10/10 10:08
 * @Email stephen.zwd@gmail.com
 */
public class Main {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(userService);
        UserService proxyInstance=(UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(),userService.getClass().getInterfaces(),proxyFactory);
        proxyInstance.getUserid("zwd");
    }
}
