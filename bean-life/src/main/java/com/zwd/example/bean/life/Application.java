package com.zwd.example.bean.life;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zwd
 * @date 2018/12/18 08:34
 * @Email stephen.zwd@gmail.com
 */
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("tinyioc.xml");
    }
}
