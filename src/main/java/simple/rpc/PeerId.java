package rpc;

import java.util.Objects;

/**
 * 地址和端口信息
 *
 * @author zhoup
 */
public class PeerId {
    public static String ANY_ADDRESS = "0.0.0.0";
    public static String LOCAL_ADDRESS = "localhost";
    public static String LOOP_ADDRESS = "127.0.0.1";
    private EndPoint endPoint;
    private String id;

    public static PeerId create(String host, int port, String id) {
        PeerId peerId = new PeerId();
        peerId.endPoint = new EndPoint(host, port);
        peerId.id = id;
        return peerId;
    }

    public static PeerId create(String host, int port) {
        return create(host, port, null);
    }

    public static PeerId create(int port) {
        return create(LOCAL_ADDRESS, port, null);
    }

    public static PeerId parse(String address) {
        Objects.requireNonNull(address);
        String[] strs = address.split(":");
        if (strs.length == 2) {
            return create(strs[0], Integer.parseInt(strs[1]));
        }
        return create(Integer.parseInt(strs[0]));
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public String getHost() {
        return endPoint.getHost();
    }

    public int getPort() {
        return endPoint.getPort();
    }

    public static class EndPoint {
        private final String host;
        private final int port;

        public EndPoint(String host, int port) {
            Objects.requireNonNull(host);
            if (port < 1) {
                throw new ErrorPortException(port + "is error port.");
            }
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String address() {
            return host + ":" + port;
        }

        private static class ErrorPortException extends RuntimeException {
            public ErrorPortException(String message) {
                super(message);
            }
        }
    }
}
