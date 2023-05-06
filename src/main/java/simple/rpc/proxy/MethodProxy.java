package rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * 代理接口中的方法
 *
 * @author zhoup
 */
public class MethodProxy implements InvocationHandler {
    private final RpcProxyMethod proxyMethod;

    public MethodProxy(RpcProxyMethod proxyMethod) {
        Objects.requireNonNull(proxyMethod);
        this.proxyMethod = proxyMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return proxyMethod.proxyMethod(proxy, method, args);
    }

    public static <T> T proxy(Class<T> interfaceClass) {
        return proxy(interfaceClass, MethodProxy.class.getClassLoader(), null);
    }

    public static <T> T proxy(Class<T> interfaceClass, RpcProxyMethod interceptor) {
        return proxy(interfaceClass, MethodProxy.class.getClassLoader(), interceptor);
    }

    public static <T> T proxy(Class<T> interfaceClass, ClassLoader classLoader, RpcProxyMethod interceptor) {
        if (interceptor == null) {
            interceptor = defaultProxyInterceptor();
        }
        Object o = Proxy.newProxyInstance(classLoader,
                new Class<?>[]{interfaceClass}, new MethodProxy(interceptor));
        return interfaceClass.cast(o);
    }

    private static RpcProxyMethod defaultProxyInterceptor() {
        return (proxy, method, args) -> {
            System.out.println("method: " + method.getName() + " has be invoked.");
            return null;
        };
    }
}
