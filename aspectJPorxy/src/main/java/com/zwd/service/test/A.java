package com.zwd.service.test;


/**
 * @author zwd
 * @date 2018/10/11 16:43
 * @Email stephen.zwd@gmail.com
 */
public class A<K,V> implements InterfaceA<K,V>{
    @Override
    public void put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    @Override
    public V get(K key) {
    return
    }

}
