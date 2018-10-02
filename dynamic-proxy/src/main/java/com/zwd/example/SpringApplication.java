package com.zwd.example;

import java.lang.reflect.Proxy;

public class SpringApplication {

    public static void main(String[] args) {
        LogicClassFir logicClassFir = new LogicClassFir();
        ProxyService targetService = new ProxyServiceImpl();
        ProxyService proxyService = (ProxyService) Proxy.newProxyInstance(ProxyCreator.class.getClassLoader(),
                new Class[]{ProxyService.class},new ProxyCreator(targetService,logicClassFir));
    proxyService.testProxy();
    }
}
