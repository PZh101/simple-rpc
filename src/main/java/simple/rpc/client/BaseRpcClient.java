package simple.rpc.client;


import simple.rpc.proxy.RpcProxy;

/**
 * Rpc客户端的默认实现，需要被重写sendMessage方法，才能真正的使用
 *
 * @author zhoup
 */
public abstract class BaseRpcClient implements RpcClient {
    private final RpcProxy proxy;

    public BaseRpcClient() {
        //初始化rpc代理类
        proxy = new RpcProxy(this);
    }

    @Override
    public <S> S getService(Class<S> service) {
        return proxy.getProxy(service);
    }
}
