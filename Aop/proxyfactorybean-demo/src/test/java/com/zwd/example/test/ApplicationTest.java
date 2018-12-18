package com.zwd.example.test;

import com.zwd.example.test.zzz.MyBeanPostProcessor;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;

/**
 * @author zwd
 * @date 2018/12/17 14:40
 * @Email stephen.zwd@gmail.com
 */
public class ApplicationTest {

    @Test
    public void test1() {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("tinyioc.xml");
//        System.out.println("user: "+applicationContext.getBean("user"));
//        System.out.println("user1: "+applicationContext.getBean("user1"));

        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new FileSystemResource("tinyioc.xml"));
        factory.addBeanPostProcessor(new MyBeanPostProcessor());
        System.out.println("user: "+factory.getBean("user"));
        System.out.println("user1: "+factory.getBean("user1"));

    }
}
