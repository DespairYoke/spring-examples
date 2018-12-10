package com.zwd.example;

import java.lang.reflect.Method;

/**
 * @author zwd
 * @date 2018/12/10 14:44
 * @Email stephen.zwd@gmail.com
 */
public class ReflectCase {

    public static void main(String[] args) throws Exception{
        Proxy target = new Proxy();
        Method method = Proxy.class.getDeclaredMethod("run");
        method.invoke(target);
    }

    static class Proxy{
        public void run() {
            System.out.println("run");
        }
    }
}
