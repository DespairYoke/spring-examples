package com.zwd.example.proxyfactorybean.demo;


import com.zwd.example.proxyfactorybean.demo.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zwd
 * @date 2018/12/12 19:44
 * @Email stephen.zwd@gmail.com
 */
public class ProxyFactoryBeanTest {


    public static void main(String[] args) throws ClassNotFoundException {


        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("proxy-factory-bean.xml");

        UserService userService = applicationContext.getBean("proxyFactoryBean", UserService.class);

        userService.getUserid("zwd");

    }
}
