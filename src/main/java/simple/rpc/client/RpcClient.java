package simple.rpc.client;


import simple.rpc.RpcMeta;

/**
 * RPC Client
 * 可以根据不同的调用需要，实现不同的RpcClient
 *
 * @author zhoup
 */
public interface RpcClient {
    /**
     * 将客户端传入的RpcMeta发送到服务端，返回从服务端获取的RpcMeta
     *
     * @param request RPC参数
     * @return RPC结果
     */
    RpcMeta sendMessage(RpcMeta request);

    /**
     * 获取某个需要RPC调用的接口代理类
     *
     * @param service 需要RPC的接口
     * @param <S>     接口的具体类型
     * @return 当前接口的代理类
     */
    <S> S getService(Class<S> service);
}
