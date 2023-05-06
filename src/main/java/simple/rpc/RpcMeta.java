package simple.rpc;

import lombok.Data;

/**
 * 存放method的meta信息，使用二进制数据进行传送
 *
 * @author zhoup
 */
@Data
public class RpcMeta {
    /**
     * 包的名字
     */
    private String packageName;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名字
     */
    private String methodName;
    /**
     * 方法签名
     */
    private String methodSignature;
    /**
     * 方法的请求参数
     */
    private String request;
    /**
     * rpc的响应结果
     */
    private String response;
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 运行状态
     */
    private int code;

    public String toJson() {
        return SerializeUtil.toJson(this);
    }

    public RpcMeta toObject(String text) {
        return SerializeUtil.toBean(text, RpcMeta.class);
    }
}
