package simple.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import simple.rpc.Const;
import simple.rpc.PeerId;
import simple.rpc.RpcMeta;
import simple.rpc.SerializeUtil;


import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * RPC服务端
 *
 * @author zhoup
 */
public class NettyRpcServer extends BaseRpcServer {
    private final PeerId peerId;

    public NettyRpcServer() {
        super();
        peerId = PeerId.create(Const.defaultPort);
    }

    public NettyRpcServer(PeerId peerId) {
        this.peerId = peerId;
    }

    public NettyRpcServer nettyRpcServer() {
        return this;
    }

    /**
     * 开启RPC服务
     */
    @Override
    public void start() throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ServerChannel());
                        }
                    });
            System.out.println("has listen the " + peerId.getPort() + " port");
            bootstrap.bind(peerId.getPort()).sync().channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    /**
     * 基于Netty的RPC逻辑
     */
    public class ServerChannel extends ChannelInboundHandlerAdapter {
        private RpcMeta response;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            response = new RpcMeta();
            if (msg instanceof ByteBuf) {
                String text = ((ByteBuf) msg).toString(StandardCharsets.UTF_8);
                System.out.println("收到" + ctx.channel().remoteAddress().toString() + "的消息为:" + text);
                RpcMeta meta = SerializeUtil.toBean(text, RpcMeta.class);
                try {
                    response = nettyRpcServer().receive(meta);
                    response.setCode(200);
                } catch (Exception e) {
                    response.setCode(500);
                    response.setResponse("报错信息：" + e.getMessage() + "\nstack:\n" + e.getCause().toString());
                }
            }

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            Objects.requireNonNull(response);
            String s = SerializeUtil.toJson(response);
            System.out.println("返回的消息：" + s);
            ctx.writeAndFlush(Unpooled.copiedBuffer(s, StandardCharsets.UTF_8));
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}
