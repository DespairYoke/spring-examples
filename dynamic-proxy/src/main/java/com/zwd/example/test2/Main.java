package com.zwd.example.test2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zwd
 * @date 2018/10/13 09:19
 * @Email stephen.zwd@gmail.com
 */
public class Main {

    static class MyIntertorHandler implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("代理前");
            method.invoke(proxy,args);
            System.out.println("代理后");
            return null;
        }


    }

    public static void main(String[] args) throws Exception{
        UserSerivce userSerivce = new ChildA();

        Class<?>[] interfaces = userSerivce.getClass().getInterfaces();
        for (Class object : interfaces) {
            System.out.println(object.getName());
        }
        System.out.println("zzz");
//        UserSerivce o = (UserSerivce)Proxy.newProxyInstance(userSerivce.getClass().getClassLoader(), UserSerivce.class.getInterfaces(), new MyIntertorHandler());

//        o.sayhello();
    }
}
