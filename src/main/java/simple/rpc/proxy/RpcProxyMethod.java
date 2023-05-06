package rpc.proxy;

import java.lang.reflect.Method;

/**
 * 代理方法
 *
 * @author zhoup
 */
public interface RpcProxyMethod {
    /**
     * 用户自己实现方法拦截
     *
     * @param proxy  生成的代理对象
     * @param method 当前被调用的方法
     * @param args   当前调用方法的具体参数
     * @return 当前调用方法的返回值
     * @throws Throwable 异常
     */
    Object proxyMethod(Object proxy, Method method, Object[] args) throws Throwable;

    /**
     * 设置被代理方法所属的接口
     * 设置服务，不太清楚，这个方法是否应该放到这里
     *
     * @param service 被代理的服务
     */
    default void setService(Class<?> service) {
    }


}
