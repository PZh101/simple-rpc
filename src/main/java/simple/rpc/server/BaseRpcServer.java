package simple.rpc.server;


import simple.rpc.RpcMeta;
import simple.rpc.SerializeUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实现RPC服务的一些通用方法，具体的通讯框架，可以自己选择，但实现类都要继承RpcServer
 * 如使用Netty实现的RpcServer{@code NettyRpcServer}
 *
 * @author zhoup
 * @see NettyRpcServer
 */
public abstract class BaseRpcServer implements RpcServer {
    //存放服务的容器
    //key是对应的接口名称
    protected final static Map<String, Object> serviceContainer = new HashMap<>();

    public <S> void registerService(S service) {
        Objects.requireNonNull(service);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) {
            serviceContainer.put(anInterface.getName(), service);
        }
    }

    /**
     * 接受远程的rpc请求
     *
     * @param rpcMeta rpc请求内容
     * @return 运行后的具体结果
     */
    public RpcMeta receive(RpcMeta rpcMeta) {
        RpcMeta meta = new RpcMeta();
        try {
            Object ret = invokeMethod(rpcMeta);
            meta.setResponse(SerializeUtil.toJson(ret));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        //一系列校验
        return meta;
    }

    /**
     * RPC服务端根据RpcMeta信息进行方法调用
     */
    private Object invokeMethod(RpcMeta rpcMeta) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object targetObject = serviceContainer.get(rpcMeta.getInterfaceName());
        if (targetObject == null) {
            throw new NoSuchMethodException(rpcMeta.getMethodName());
        }
        for (Method declaredMethod : targetObject.getClass().getDeclaredMethods()) {
            if (rpcMeta.getMethodSignature().equals(SerializeUtil.getUniqueMethodName(declaredMethod))) {
                //调用具体的方法
                Object[] args = SerializeUtil.toArray(rpcMeta.getRequest());
                return declaredMethod.invoke(targetObject, args);
            }
        }
        return null;
    }

}
