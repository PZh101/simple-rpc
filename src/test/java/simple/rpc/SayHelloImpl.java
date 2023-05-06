package simple.rpc;

/**
 * @author zhoup
 * @since 2023/5/6
 */
public class SayHelloImpl implements SayHello {
    @Override
    public String sayHello(String msg) {
        String result = "sayHelloImpl say:" + msg;
        System.out.println(result);
        return result;
    }
}
