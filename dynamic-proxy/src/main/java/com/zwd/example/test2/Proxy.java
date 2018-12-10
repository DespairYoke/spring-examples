//package com.zwd.example.test2;
//
//import sun.reflect.CallerSensitive;
//import sun.reflect.Reflection;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Modifier;
//import java.security.AccessController;
//import java.security.PrivilegedAction;
//import java.util.Objects;
//
///**
// * @author zwd
// * @date 2018/12/10 15:00
// * @Email stephen.zwd@gmail.com
// */
////代理类
//public class Proxy implements java.io.Serializable {
//
//    private static final long serialVersionUID = -2222568056686623797L;
//
//    //代理类构造函数的参数类型
//    private static final Class<?>[] constructorParams =
//            { InvocationHandler.class };
//
//    //代理类缓存
//    private static final WeakCache<ClassLoader, Class<?>[], Class<?>>
//            proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());
//
//
//    //此代理实例的调用处理程序。
//    protected InvocationHandler h;
//
//    private Proxy() {
//    }
//
//    //代理类构造函数，参数类型：constructorParams所指定
//    protected Proxy(InvocationHandler h) {
//        Objects.requireNonNull(h);
//        this.h = h;
//    }
//
//
//    //获取目标代理类Class对象，需传入类加载器loader对象和被代理类实现接口数组interfaces随想
//    @CallerSensitive
//    public static Class<?> getProxyClass(ClassLoader loader,Class<?>... interfaces)
//            throws IllegalArgumentException{
//        //拷贝接口数组
//        final Class<?>[] intfs = interfaces.clone();
//        final SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            //校验代理类的访问问权限
//            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
//        }
//        //获取代理类Class对象
//        return getProxyClass0(loader, intfs);
//    }
//    //校验代理类的访问问权限,这一块比较底层，我也不明白
//    private static void checkProxyAccess(Class<?> caller, ClassLoader loader, Class<?>... interfaces){
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            //获取调用者类的类加载器
//            ClassLoader ccl = caller.getClassLoader();
//            if (VM.isSystemDomainLoader(loader) && !VM.isSystemDomainLoader(ccl)) {
//                sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
//            }
//            ReflectUtil.checkProxyPackageAccess(ccl, interfaces);
//        }
//    }
//    //获取代理类的Clas对象
//    private static Class<?> getProxyClass0(ClassLoader loader,
//                                           Class<?>... interfaces) {
//        if (interfaces.length > 65535) {
//            throw new IllegalArgumentException("interface limit exceeded");
//        }
//
//        //如果存在给定接口的给定装入器定义的代理类存在，则只返回缓存的副本；
//        //否则，它将通过proxyclassfactory创建代理类
//        //jdk1.8后收敛到这里 生成代理类字节码过程: ProxyClassFactory中了
//        return proxyClassCache.get(loader, interfaces);
//    }
//
//
//    //用于带有0个实现接口的代理类的key键值
//    private static final Object key0 = new Object();
//
//    /*
//     * Key1 and Key2 are optimized for the common use of dynamic proxies
//     * that implement 1 or 2 interfaces.
//     */
//
//    /*
//     * a key used for proxy class with 1 implemented interface
//     */
//    //用于带有1个实现接口的代理类的key键值
//    private static final class Key1 extends WeakReference<Class<?>> {
//        private final int hash;
//
//        Key1(Class<?> intf) {
//            super(intf);
//            this.hash = intf.hashCode();
//        }
//
//        @Override
//        public int hashCode() {
//            return hash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            Class<?> intf;
//            return this == obj ||
//                    obj != null &&
//                            obj.getClass() == Key1.class &&
//                            (intf = get()) != null &&
//                            intf == ((Key1) obj).get();
//        }
//    }
//
//    // //用于带有2个实现接口的代理类的key键值
//    private static final class Key2 extends WeakReference<Class<?>> {
//        private final int hash;
//        //弱引用对象：存放第二个接口类对象
//        private final WeakReference<Class<?>> ref2;
//
//        Key2(Class<?> intf1, Class<?> intf2) {
//            super(intf1);
//            hash = 31 * intf1.hashCode() + intf2.hashCode();
//            ref2 = new WeakReference<Class<?>>(intf2);
//        }
//
//        @Override
//        public int hashCode() {
//            return hash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            Class<?> intf1, intf2;
//            return this == obj ||
//                    obj != null &&
//                            obj.getClass() == Key2.class &&
//                            (intf1 = get()) != null &&
//                            intf1 == ((Key2) obj).get() &&
//                            (intf2 = ref2.get()) != null &&
//                            intf2 == ((Key2) obj).ref2.get();
//        }
//    }
//
//
//    // 这里用于带有>=3个实现接口的代理类的key键值（可以当实现任意数目接口的代理的key）
//    private static final class KeyX {
//        //接口数组对象的hash值
//        private final int hash;
//        //弱引用对象数组：存放表示接口的类对象数组
//        private final WeakReference<Class<?>>[] refs;
//
//        @SuppressWarnings("unchecked")
//        KeyX(Class<?>[] interfaces) {
//            hash = Arrays.hashCode(interfaces);
//            //构造一个弱引用对象数组
//            refs = (WeakReference<Class<?>>[])new WeakReference<?>[interfaces.length];
//            for (int i = 0; i < interfaces.length; i++) {
//                refs[i] = new WeakReference<>(interfaces[i]);
//            }
//        }
//
//        @Override
//        public int hashCode() {
//            return hash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return this == obj ||
//                    obj != null &&
//                            obj.getClass() == KeyX.class &&
//                            equals(refs, ((KeyX) obj).refs);
//        }
//
//        private static boolean equals(WeakReference<Class<?>>[] refs1,
//                                      WeakReference<Class<?>>[] refs2) {
//            //长度是否相等
//            if (refs1.length != refs2.length) {
//                return false;
//            }
//            //弱引用对象数组内接口类是否相同
//            for (int i = 0; i < refs1.length; i++) {
//                Class<?> intf = refs1[i].get();
//                if (intf == null || intf != refs2[i].get()) {
//                    return false;
//                }
//            }
//            return true;
//        }
//    }
//
//
//    //将接口数组映射到一个最佳键的函数，其中表示接口的类对象为弱引用。
//    private static final class KeyFactory
//            implements BiFunction<ClassLoader, Class<?>[], Object>
//    {
//        @Override
//        public Object apply(ClassLoader classLoader, Class<?>[] interfaces) {
//            switch (interfaces.length) {
//                case 1: return new Key1(interfaces[0]); // the most frequent
//                case 2: return new Key2(interfaces[0], interfaces[1]);
//                case 0: return key0;
//                default: return new KeyX(interfaces);
//            }
//        }
//    }
//
//
//    //一个工厂函数生成‘给定类装载器和接口数组的代理类’。
//    private static final class ProxyClassFactory
//            implements BiFunction<ClassLoader, Class<?>[], Class<?>>
//    {
//        //所有代理类名称的前缀
//        private static final String proxyClassNamePrefix = "$Proxy";
//
//        //用于生成唯一代理类名称的下一个数字
//        private static final AtomicLong nextUniqueNumber = new AtomicLong();
//
//        @Override
//        public Class<?> apply(ClassLoader loader, Class<?>[] interfaces) {
//
//            Map<Class<?>, Boolean> interfaceSet = new IdentityHashMap<>(interfaces.length);
//            for (Class<?> intf : interfaces) {
//
//                //验证intf接口类Class对象是否为给定的classloder解析的
//                Class<?> interfaceClass = null;
//                try {
//                    //指定类加载器获取接口类class对象，
//                    interfaceClass = Class.forName(intf.getName(), false, loader);
//                } catch (ClassNotFoundException e) {
//                }
//                //验证是否为同一个接口类对象，
//                if (interfaceClass != intf) {
//                    throw new IllegalArgumentException(
//                            intf + " is not visible from class loader");
//                }
//                //验证类对象是否为接口
//                if (!interfaceClass.isInterface()) {
//                    throw new IllegalArgumentException(
//                            interfaceClass.getName() + " is not an interface");
//                }
//                /*
//                 * Verify that this interface is not a duplicate.
//                 * 验证此接口不是副本。
//                 */
//                if (interfaceSet.put(interfaceClass, Boolean.TRUE) != null) {
//                    throw new IllegalArgumentException(
//                            "repeated interface: " + interfaceClass.getName());
//                }
//            }
//            //定义代理类的包
//            String proxyPkg = null;
//            //定义代理类的修饰符：public和final类型
//            int accessFlags = Modifier.PUBLIC | Modifier.FINAL;
//
//            /*
//             * Record the package of a non-public proxy interface so that the
//             * proxy class will be defined in the same package.  Verify that
//             * all non-public proxy interfaces are in the same package.
//             * 记录一个非公共代理接口的包，以便在同一个包中定义代理类。验证所有非公共代理接口都在同一个包中。
//             */
//            for (Class<?> intf : interfaces) {
//                //获取修饰符
//                int flags = intf.getModifiers();
//                if (!Modifier.isPublic(flags)) {  //不是public修饰符
//                    //则定义代理类的修饰符符：final
//                    accessFlags = Modifier.FINAL;
//                    //获取接口类名称，如：com.text.MyInterface
//                    String name = intf.getName();
//                    int n = name.lastIndexOf('.');
//                    //获取包名，如：com.text
//                    String pkg = ((n == -1) ? "" : name.substring(0, n + 1));
//                    if (proxyPkg == null) {
//                        //获取包名称
//                        proxyPkg = pkg;
//                    } else if (!pkg.equals(proxyPkg)) { //interfaces含有来自不同包的非公共接口,抛错
//                        throw new IllegalArgumentException(
//                                "non-public interfaces from different packages");
//                    }
//                }
//            }
//
//            if (proxyPkg == null) {
//                //如果没有非公开的代理接口，使用com.sun.proxy作为包名称
//                proxyPkg = ReflectUtil.PROXY_PACKAGE + ".";
//            }
//
//            //选择要生成的代理类的名称。
//            long num = nextUniqueNumber.getAndIncrement();
//            String proxyName = proxyPkg + proxyClassNamePrefix + num;
//
//            //真正生成指定的代理类字节码的地方
//            byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
//                    proxyName, interfaces, accessFlags);
//            try {
//                //生成Calss对象
//                return defineClass0(loader, proxyName, proxyClassFile, 0, proxyClassFile.length);
//            } catch (ClassFormatError e) {
//                /*
//                 * A ClassFormatError here means that (barring bugs in the
//                 * proxy class generation code) there was some other
//                 * invalid aspect of the arguments supplied to the proxy
//                 * class creation (such as virtual machine limitations
//                 * exceeded).
//                 */
//                throw new IllegalArgumentException(e.toString());
//            }
//        }
//    }
//
//    //生成代理类对象
//    @CallerSensitive
//    public static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h)
//            throws IllegalArgumentException {
//        //调用处理程序不能为空
//        Objects.requireNonNull(h);
//        //拷贝接口数组对象
//        final Class<?>[] intfs = interfaces.clone();
//        final SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
//        }
//
//        //查找或生成指定的代理类。
//        Class<?> cl = getProxyClass0(loader, intfs);
//
//        /*
//         * Invoke its constructor with the designated invocation handler.
//         * 调用指定的调用处理程序的构造函数
//         */
//        try {
//            if (sm != null) {
//                //校验新代理类的权限
//                checkNewProxyPermission(Reflection.getCallerClass(), cl);
//            }
//            //获取代理类构造函数，参数类型必须为InvocationHandler
//            final Constructor<?> cons = cl.getConstructor(constructorParams);
//            final InvocationHandler ih = h;
//            //构造函数不是public，则设置当前构造函数为访问权限
//            if (!Modifier.isPublic(cl.getModifiers())) {
//                AccessController.doPrivileged(new PrivilegedAction<Void>() {
//                    public Void run() {
//                        cons.setAccessible(true);
//                        return null;
//                    }
//                });
//            }
//            //调用构造函数构造代理类实例，入参数为‘调用处理程序’的实例,看到这里应该就明白jdk怎么实现动态代理的吧！
//            return cons.newInstance(new Object[]{h});
//        } catch (IllegalAccessException|InstantiationException e) {
//            throw new InternalError(e.toString(), e);
//        } catch (InvocationTargetException e) {
//            Throwable t = e.getCause();
//            if (t instanceof RuntimeException) {
//                throw (RuntimeException) t;
//            } else {
//                throw new InternalError(t.toString(), t);
//            }
//        } catch (NoSuchMethodException e) {
//            throw new InternalError(e.toString(), e);
//        }
//    }
//
//    private static void checkNewProxyPermission(Class<?> caller, Class<?> proxyClass) {
//        SecurityManager sm = System.getSecurityManager();
//        if (sm != null) {
//            if (ReflectUtil.isNonPublicProxyClass(proxyClass)) {
//                //调用者类加载器
//                ClassLoader ccl = caller.getClassLoader();
//                //代理类的类加载器
//                ClassLoader pcl = proxyClass.getClassLoader();
//
//                // do permission check if the caller is in a different runtime package
//                // of the proxy class
//                //获取代理类的包名
//                int n = proxyClass.getName().lastIndexOf('.');
//                String pkg = (n == -1) ? "" : proxyClass.getName().substring(0, n);
//                //获取调用者包名
//                n = caller.getName().lastIndexOf('.');
//                String callerPkg = (n == -1) ? "" : caller.getName().substring(0, n);
//                //类加载不相同或包名不相同，校验权限
//                if (pcl != ccl || !pkg.equals(callerPkg)) {
//                    sm.checkPermission(new ReflectPermission("newProxyInPackage." + pkg));
//                }
//            }
//        }
//    }
//
//    public static boolean isProxyClass(Class<?> cl) {
//        return Proxy.class.isAssignableFrom(cl) && proxyClassCache.containsValue(cl);
//    }
//
//    @CallerSensitive
//    public static InvocationHandler getInvocationHandler(Object proxy)
//            throws IllegalArgumentException
//    {
//
//        //验证该对象实际上是否为上一个代理实例。
//        if (!isProxyClass(proxy.getClass())) {
//            throw new IllegalArgumentException("not a proxy instance");
//        }
//
//        final Proxy p = (Proxy) proxy;
//        final InvocationHandler ih = p.h;
//        if (System.getSecurityManager() != null) {
//            Class<?> ihClass = ih.getClass();
//            Class<?> caller = Reflection.getCallerClass();
//            if (ReflectUtil.needsPackageAccessCheck(caller.getClassLoader(),
//                    ihClass.getClassLoader()))
//            {
//                ReflectUtil.checkPackageAccess(ihClass);
//            }
//        }
//
//        return ih;
//    }
//
//    private static native Class<?> defineClass0(ClassLoader loader, String name,
//                                                byte[] b, int off, int len);
//}
//---------------------
//        作者：灵小帝
//        来源：CSDN
//        原文：https://blog.csdn.net/lh513828570/article/details/74078773
//        版权声明：本文为博主原创文章，转载请附上博文链接！