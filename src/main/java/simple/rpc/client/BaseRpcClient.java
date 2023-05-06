package simple.rpc.client;


import simple.rpc.RpcMeta;
import simple.rpc.proxy.RpcProxy;

/**
 * Rpc客户端的默认实现，需要被重写sendMessage方法，才能真正的使用
 *
 * @author zhoup
 */
public class DefaultRpcClient implements RpcClient {
    private final RpcProxy proxy;

    public DefaultRpcClient() {
        //初始化rpc代理类
        proxy = new RpcProxy(this);
    }

    /**
     * 将消息发送到服务端
     *
     * @param rpcMessage 该消息体目前必须是支持json化
     * @return 服务端的响应
     */
    @Override
    public RpcMeta sendMessage(RpcMeta rpcMessage) {
//        String metaStr = SerializeUtil.toJson(rpcMessage);
//        System.out.println("需要发送的请求:");
//        System.out.println(metaStr);
        return rpcMessage;
    }

    @Override
    public <S> S getService(Class<S> service) {
        return proxy.getProxy(service);
    }
}
