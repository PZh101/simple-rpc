package simple.rpc.proxy;



import simple.rpc.RpcMeta;
import simple.rpc.SerializeUtil;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 基本的代理类
 *
 * @author zhoup
 */
public abstract class BaseRpcRpcProxy implements RpcProxyMethod {
    protected Class<?> service;

    @Override
    public void setService(Class<?> service) {
        this.service = service;
    }

    /**
     * 获取接口的名称
     *
     * @return 接口名称
     */
    public String getInterfaceName() {
        Objects.requireNonNull(service);
        return service.getName();
    }

    /**
     * 根据方法构建具体的RpcMeta
     *
     * @param method 接口的方法
     * @param args   传入的方法参数
     * @return 构建好的rpcMeta
     */
    public RpcMeta createMeta(Method method, Object[] args) {
        RpcMeta rpcMeta = new RpcMeta();
        rpcMeta.setMethodSignature(SerializeUtil.getUniqueMethodName(method));
        String methodParameters = "[]";
        if (args != null) {
            methodParameters = SerializeUtil.toJson(args);
        }
        rpcMeta.setRequest(methodParameters);
        rpcMeta.setInterfaceName(getInterfaceName());
        return rpcMeta;
    }
}
