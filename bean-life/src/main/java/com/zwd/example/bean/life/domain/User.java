package com.zwd.example.bean.life.domain;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author zwd
 * @date 2018/12/17 14:10
 * @Email stephen.zwd@gmail.com
 *
 */
public class User implements BeanNameAware,BeanFactoryAware,InitializingBean,ApplicationContextAware,DisposableBean{

    private String id;

    private String name;

    public void setBeanName(String id) {
        System.out.println(" i am beanName");
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("beanFactory");
    }


    public void destroy() throws Exception {

        System.out.println("i am destory");
    }

    public void afterPropertiesSet() throws Exception {

        System.out.println("i am afterPropertiesSet");
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("applicationContext");
    }
}
