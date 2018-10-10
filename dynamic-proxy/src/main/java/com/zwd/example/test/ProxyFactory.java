package com.zwd.example.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zwd
 * @date 2018/10/10 10:13
 * @Email stephen.zwd@gmail.com
 */
public class ProxyFactory implements InvocationHandler{

    private Object target;

    public ProxyFactory(Object target) {
        this.target =  target;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("前置通知,"+ args[0]);
        Object result = method.invoke(target,args);
        System.out.println("后置通知，");
        return result;
    }

}
