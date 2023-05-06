package rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.lang.reflect.Method;

/**
 * 序列化工具，方便后续替换，如使用proto进行传输
 * 目前使用的是Json字符串进行消息传输
 * @author zhoup
 */
public class SerializeUtil {
    public static Object toObject(String text) {
        if (text.equals("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        return JSON.parse(text);
    }

    public static Object[] toArray(String text) {
        Object parse = JSON.parse(text);
        if (parse instanceof JSONArray) {
            return ((JSONArray) parse).toArray();
        }
        return new Object[]{parse};
    }

    public static <T> T toBean(String text, Class<T> clazz) {
        if (text.equals("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        return JSON.parseObject(text, clazz);
    }

    public static String toJson(Object object) {
        return JSON.toJSONString(object) + "\n";
    }

    /**
     * 只会对比方法的名，入参，和返回类型
     *
     * @param method 具体的方法
     * @return 生成的名称
     */
    public static String getUniqueMethodName(Method method) {
        StringBuilder signature = new StringBuilder();
        signature.append(method.getReturnType().getName())
                .append("#")
                .append(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 0) {
            signature.append("(");
            for (Class<?> parameterType : parameterTypes) {
                signature.append("@").append(parameterType.getCanonicalName());
            }
        }
        return signature.toString();
    }
}
