package simple.rpc.proxy;



import simple.rpc.RpcMeta;
import simple.rpc.SerializeUtil;
import simple.rpc.client.RpcClient;

import java.lang.reflect.Method;

/**
 * RpcProxyMethod的默认实现
 *
 * @author zhoup
 */
public class DefaultRpcRpcProxyClient extends BaseRpcRpcProxy {
    private final RpcClient rpcClient;

    public DefaultRpcRpcProxyClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Object proxyMethod(Object proxy, Method method, Object[] args) throws Throwable {
        RpcMeta meta = rpcClient.sendMessage(createMeta(method, args));
        if (meta == null) {
            return null;
        }
        //返回响应信息
        String response = meta.getResponse();
        Object parse = SerializeUtil.toObject(response);
        Class<?> returnType = method.getReturnType();
        return returnType.cast(parse);
    }
}
