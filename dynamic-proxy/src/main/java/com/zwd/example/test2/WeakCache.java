//package com.zwd.example.test2;
//
///**
// * @author zwd
// * @date 2018/12/10 15:01
// * @Email stephen.zwd@gmail.com
// */
////用处存放
//final class WeakCache<K, P, V> {
//
//    private final ReferenceQueue<K> refQueue = new ReferenceQueue<>();
//    // the key type is Object for supporting null key
//    //Map<类加载器，Map<接口数组对象key，代理类工厂Factory或代理类包装对象LookupValue>
//    private final ConcurrentMap<Object, ConcurrentMap<Object, Supplier<V>>> map  = new ConcurrentHashMap<>();
//    //存放当前key所对应的实例是否已经生成
//    private final ConcurrentMap<Supplier<V>, Boolean> reverseMap = new ConcurrentHashMap<>();
//    //生成key的工厂（对应上面的Key1-X类）
//    private final BiFunction<K, P, ?> subKeyFactory;
//    //生成value（代理类工厂，对应上面的ProxyClassFactory类）
//    private final BiFunction<K, P, V> valueFactory;
//
//
//    //构造方法,指定key和value工厂
//    public WeakCache(BiFunction<K, P, ?> subKeyFactory,
//                     BiFunction<K, P, V> valueFactory) {
//        this.subKeyFactory = Objects.requireNonNull(subKeyFactory);
//        this.valueFactory = Objects.requireNonNull(valueFactory);
//    }
//
//
//    //获取缓存值，
//    public V get(K key, P parameter) {
//        //parameter不能weinull
//        Objects.requireNonNull(parameter);
//        //删除老节点
//        expungeStaleEntries();
//
//        Object cacheKey = CacheKey.valueOf(key, refQueue);
//
//        // lazily install the 2nd level valuesMap for the particular cacheKey
//        ConcurrentMap<Object, Supplier<V>> valuesMap = map.get(cacheKey);
//        if (valuesMap == null) {
//            //添加cacheKey - ConcurrentMap<Object, Supplier<V>对象到map中
//            ConcurrentMap<Object, Supplier<V>> oldValuesMap = map.putIfAbsent(cacheKey,valuesMap = new ConcurrentHashMap<>());
//            if (oldValuesMap != null) {
//                valuesMap = oldValuesMap;
//            }
//        }
//
//        //利用subKeyFactory生成subKey
//        Object subKey = Objects.requireNonNull(subKeyFactory.apply(key, parameter));
//        //获取valuesMap中subKey所对应的值，此时为cachevalue <V>实例（步骤1：cachevalue <V>实例的来源）
//        Supplier<V> supplier = valuesMap.get(subKey);
//        Factory factory = null;
//
//        while (true) {
//            if (supplier != null) {
//                //步骤1：supplier可能是一个工厂对象或一个cachevalue <V>实例
//                V value = supplier.get();
//                if (value != null) {
//                    return value;
//                }
//            }
//            // else no supplier in cache
//            // or a supplier that returned null (could be a cleared CacheValue
//            // or a Factory that wasn't successful in installing the CacheValue)
//
//            // lazily construct a Factory
//            if (factory == null) {
//                //构造一个工厂对象(步骤1：工厂对象的来源）
//                factory = new Factory(key, parameter, subKey, valuesMap);
//            }
//            //
//            if (supplier == null) {
//                //factory添加在valuesMap中
//                supplier = valuesMap.putIfAbsent(subKey, factory);
//                if (supplier == null) {
//                    // 添加成功，则factory赋值给supplier，此时supplier为一个工厂对象
//                    supplier = factory;
//                }
//                // else retry with winning supplier
//                //失败则重试
//            } else {
//                //将valuesMap中subKey所对应的值替换为factory
//                if (valuesMap.replace(subKey, supplier, factory)) {
//                    // successfully replaced
//                    // cleared CacheEntry / unsuccessful Factory
//                    // with our Factory
//                    supplier = factory;
//                } else {
//                    // retry with current supplier
//                    supplier = valuesMap.get(subKey);
//                }
//            }
//        }
//    }
//
//    /**
//     * Checks whether the specified non-null value is already present in this
//     * {@code WeakCache}. The check is made using identity comparison regardless
//     * of whether value's class overrides {@link Object#equals} or not.
//     *
//     * @param value the non-null value to check
//     * @return true if given {@code value} is already cached
//     * @throws NullPointerException if value is null
//     */
//    public boolean containsValue(V value) {
//        Objects.requireNonNull(value);
//
//        expungeStaleEntries();
//        return reverseMap.containsKey(new LookupValue<>(value));
//    }
//
//    /**
//     * Returns the current number of cached entries that
//     * can decrease over time when keys/values are GC-ed.
//     */
//    public int size() {
//        expungeStaleEntries();
//        return reverseMap.size();
//    }
//
//    private void expungeStaleEntries() {
//        CacheKey<K> cacheKey;
//        while ((cacheKey = (CacheKey<K>)refQueue.poll()) != null) {
//            cacheKey.expungeFrom(map, reverseMap);
//        }
//    }
//
//    /**
//     * A factory {@link Supplier} that implements the lazy synchronized
//     * construction of the value and installment of it into the cache.
//     */
//    private final class Factory implements Supplier<V> {
//
//        private final K key;
//        private final P parameter;
//        private final Object subKey;
//        private final ConcurrentMap<Object, Supplier<V>> valuesMap;
//
//        Factory(K key, P parameter, Object subKey,
//                ConcurrentMap<Object, Supplier<V>> valuesMap) {
//            this.key = key;
//            this.parameter = parameter;
//            this.subKey = subKey;
//            this.valuesMap = valuesMap;
//        }
//
//        @Override
//        public synchronized V get() { // serialize access
//            //校验是否为同一个
//            Supplier<V> supplier = valuesMap.get(subKey);
//            if (supplier != this) {
//                //在我们等待的时候，可能某些值已经被替换或则删除，返回null让WeakCache.get()再次循环获取
//                return null;
//            }
//            //创建一个新值
//            V value = null;
//            try {
//                //调用valueFactory的applay方法生成一个新值
//                value = Objects.requireNonNull(valueFactory.apply(key, parameter));
//            } finally {
//                if (value == null) { //如果生成失败，从valuesMap中删除值
//                    valuesMap.remove(subKey, this);
//                }
//            }
//            // the only path to reach here is with non-null value
//            assert value != null;
//
//            // wrap value with CacheValue (WeakReference)
//            CacheValue<V> cacheValue = new CacheValue<>(value);
//
//            //尝试更换我们cachevalue（这应该总是成功的)此时
//            if (valuesMap.replace(subKey, this, cacheValue)) {
//                // 放在reversemap
//                reverseMap.put(cacheValue, Boolean.TRUE);
//            } else {
//                throw new AssertionError("Should not reach here");
//            }
//            //成功取代当前工厂对象为一个新的CacheValue（包裹着我们的需要对象：代理类）
//            return value;
//        }
//    }
//
//    //Value接口
//    private interface Value<V> extends Supplier<V> {}
//
//    //值对象：包裹着我们的需要对象：代理类
//    private static final class LookupValue<V> implements Value<V> {
//        private final V value;
//
//        LookupValue(V value) {
//            this.value = value;
//        }
//
//        @Override
//        public V get() {
//            return value;
//        }
//
//        @Override
//        public int hashCode() {
//            return System.identityHashCode(value); // compare by identity
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return obj == this ||
//                    obj instanceof Value &&
//                            this.value == ((Value<?>) obj).get();  // compare by identity
//        }
//    }
//
//    /**
//     * A {@link Value} that weakly references the referent.
//     */
//    private static final class CacheValue<V>
//            extends WeakReference<V> implements Value<V>
//    {
//        private final int hash;
//
//        CacheValue(V value) {
//            super(value);
//            this.hash = System.identityHashCode(value); // compare by identity
//        }
//
//        @Override
//        public int hashCode() {
//            return hash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            V value;
//            return obj == this ||
//                    obj instanceof Value &&
//                            // cleared CacheValue is only equal to itself
//                            (value = get()) != null &&
//                            value == ((Value<?>) obj).get(); // compare by identity
//        }
//    }
//
//    private static final class CacheKey<K> extends WeakReference<K> {
//
//        // a replacement for null keys
//        private static final Object NULL_KEY = new Object();
//
//        static <K> Object valueOf(K key, ReferenceQueue<K> refQueue) {
//            return key == null
//                    // null key means we can't weakly reference it,
//                    // so we use a NULL_KEY singleton as cache key
//                    ? NULL_KEY
//                    // non-null key requires wrapping with a WeakReference
//                    : new CacheKey<>(key, refQueue);
//        }
//
//        private final int hash;
//
//        private CacheKey(K key, ReferenceQueue<K> refQueue) {
//            super(key, refQueue);
//            this.hash = System.identityHashCode(key);  // compare by identity
//        }
//
//        @Override
//        public int hashCode() {
//            return hash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            K key;
//            return obj == this ||
//                    obj != null &&
//                            obj.getClass() == this.getClass() &&
//                            // cleared CacheKey is only equal to itself
//                            (key = this.get()) != null &&
//                            // compare key by identity
//                            key == ((CacheKey<K>) obj).get();
//        }
//
//        void expungeFrom(ConcurrentMap<?, ? extends ConcurrentMap<?, ?>> map,
//                         ConcurrentMap<?, Boolean> reverseMap) {
//            // removing just by key is always safe here because after a CacheKey
//            // is cleared and enqueue-ed it is only equal to itself
//            // (see equals method)...
//            ConcurrentMap<?, ?> valuesMap = map.remove(this);
//            // remove also from reverseMap if needed
//            if (valuesMap != null) {
//                for (Object cacheValue : valuesMap.values()) {
//                    reverseMap.remove(cacheValue);
//                }
//            }
//        }
//    }
//}
//---------------------
//        作者：灵小帝
//        来源：CSDN
//        原文：https://blog.csdn.net/lh513828570/article/details/74078773
//        版权声明：本文为博主原创文章，转载请附上博文链接！