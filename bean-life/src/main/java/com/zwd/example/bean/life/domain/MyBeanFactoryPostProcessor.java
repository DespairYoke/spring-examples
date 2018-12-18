package com.zwd.example.bean.life.domain;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author zwd
 * @date 2018/12/18 08:42
 * @Email stephen.zwd@gmail.com
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor{

    public MyBeanFactoryPostProcessor() {
        System.out.println(" i am MybeanFactoryPostProcessor的构造");
    }
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("i am beanFactoryPostProcessor");
    }
}
