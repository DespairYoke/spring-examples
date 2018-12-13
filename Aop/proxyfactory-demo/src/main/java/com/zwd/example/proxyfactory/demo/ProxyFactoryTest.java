package com.zwd.example.proxyfactory.demo;


import com.zwd.example.proxyfactory.demo.advice.MybeforeAdvice;
import com.zwd.example.proxyfactory.demo.service.UserService;
import com.zwd.example.proxyfactory.demo.service.UserServiceImpl;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author zwd
 * @date 2018/12/10 14:44
 * @Email stephen.zwd@gmail.com
 */
public class ProxyFactoryTest {

    public static void main(String[] args) throws Exception{
        UserService userService = new UserServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(userService);

        proxyFactory.addAdvice(new MybeforeAdvice());

        UserService proxy=(UserService) proxyFactory.getProxy();

        proxy.getUserid("zzz");

    }


}
