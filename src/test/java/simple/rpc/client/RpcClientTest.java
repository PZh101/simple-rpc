package simple.rpc.client;

import org.junit.jupiter.api.Test;
import simple.rpc.SayHello;

/**
 * @author zhoup
 * @since 2023/5/6
 */
class RpcClientTest {


    @Test
    void getService() {
        RpcClient rpcClient = new NettyRpcClient();
        SayHello service = rpcClient.getService(SayHello.class);
        String result = service.sayHello("大家好");
        System.out.println(result);

    }
}