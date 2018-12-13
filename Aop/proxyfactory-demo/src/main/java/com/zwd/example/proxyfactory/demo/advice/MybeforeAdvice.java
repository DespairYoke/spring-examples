package com.zwd.example.proxyfactory.demo.advice;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author zwd
 * @date 2018/12/10 16:49
 * @Email stephen.zwd@gmail.com
 */
public class MybeforeAdvice implements MethodBeforeAdvice{
    @Override
    public void before(java.lang.reflect.Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("前置通知");
        method.invoke(o,objects);
    }
}
