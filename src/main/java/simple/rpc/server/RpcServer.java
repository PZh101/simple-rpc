package simple.rpc.server;

/**
 * RPC server接口
 *
 * @author zhoup
 * @since 2023/5/6
 */
public interface RpcServer {
    /**
     * 向RPC的Server注册服务
     */
    <S> void registerService(S service);

    /**
     * 启动RpcServer
     *
     * @throws InterruptedException 异常信息
     */
    void start() throws InterruptedException;
}
