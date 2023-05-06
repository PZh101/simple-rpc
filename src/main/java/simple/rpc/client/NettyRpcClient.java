package simple.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import simple.rpc.Const;
import simple.rpc.PeerId;
import simple.rpc.RpcMeta;
import simple.rpc.SerializeUtil;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * RPC客户端
 *
 * @author zhoup
 */
@Slf4j
public class NettyRpcClient extends BaseRpcClient {
    private final PeerId peerId;

    public NettyRpcClient() {
        super();
        peerId = PeerId.create(Const.defaultPort);
    }

    public NettyRpcClient(PeerId peerId) {
        super();
        this.peerId = peerId;
    }

    /**
     * 使用Netty框架发送，RPC请求
     *
     * @param request RPC请求
     * @return RPC响应结果
     * @throws InterruptedException 异常
     */
    private RpcMeta invokeRpc(RpcMeta request) throws InterruptedException {
        RpcMeta response;
        NettyClientChannel nettyClientChannel = new NettyClientChannel(request);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(nettyClientChannel);
                        }
                    });
            ChannelFuture f = bootstrap.connect(peerId.getHost(), peerId.getPort()).sync();
            f.channel().closeFuture().sync();
            response = nettyClientChannel.getResponse();
        } finally {
            workGroup.shutdownGracefully();
        }
        return response;
    }


    @Override
    public RpcMeta sendMessage(RpcMeta rpcMessage) {
        RpcMeta response = new RpcMeta();
        try {
            response = invokeRpc(rpcMessage);
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
        return response;
    }

    /**
     * Netty实现RPC请求发送与响应接收的具体实现
     *
     * @author zhoup
     */
    @Slf4j
    private static class NettyClientChannel extends ChannelInboundHandlerAdapter {
        private final RpcMeta request;
        private RpcMeta response;

        public NettyClientChannel(RpcMeta request) {
            Objects.requireNonNull(request);
            this.request = request;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String s = SerializeUtil.toJson(request);
            ctx.writeAndFlush(Unpooled.copiedBuffer(s, StandardCharsets.UTF_8));
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof ByteBuf) {
                String text = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);
                log.info("收到服务端的消息:" + text);
                response = SerializeUtil.toBean(text, RpcMeta.class);
            } else {
                log.info("收到服务端的消息:" + msg);
            }
            ctx.close();
        }

        public RpcMeta getResponse() {
            return response;
        }
    }
}
