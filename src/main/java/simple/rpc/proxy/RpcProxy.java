package rpc.proxy;



import rpc.client.DefaultRpcClient;

import java.util.Objects;

/**
 * 生成代理
 *
 * @author zhoup
 */
public class RpcProxy {
    private final RpcProxyMethod rpcProxyMethod;

    public RpcProxy(RpcProxyMethod rpcProxyMethod) {
        this.rpcProxyMethod = rpcProxyMethod;
    }

    public RpcProxy(DefaultRpcClient rpcClient) {
        //创建rpc具体的执行逻辑
        rpcProxyMethod = new DefaultRpcRpcProxyClient(rpcClient);
    }

    public <T> T getProxy(Class<T> service) {
        Objects.requireNonNull(service);
        rpcProxyMethod.setService(service);
        return MethodProxy.proxy(service, rpcProxyMethod);
    }
}
