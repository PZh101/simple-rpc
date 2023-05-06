package simple.rpc.server;

import org.junit.jupiter.api.Test;
import simple.rpc.SayHelloImpl;

/**
 * @author zhoup
 * @since 2023/5/6
 */
class RpcServerTest {

    @Test
    void registerService() {
        RpcServer rpcServer = new NettyRpcServer();
        rpcServer.registerService(new SayHelloImpl());
    }

    @Test
    void start() throws InterruptedException {
        RpcServer rpcServer = new NettyRpcServer();
        rpcServer.registerService(new SayHelloImpl());
        rpcServer.start();
    }
}