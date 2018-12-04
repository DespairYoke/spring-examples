package com.zwd.service.test;

/**
 * @author zwd
 * @date 2018/10/13 10:13
 * @Email stephen.zwd@gmail.com
 */
public interface InterfaceA<K,V> {

    void put(K key,V value);

    V get(K key);
}
