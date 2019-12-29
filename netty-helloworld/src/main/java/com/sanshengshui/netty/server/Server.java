package com.sanshengshui.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author 穆书伟
 * @date 2018年9月18号
 * @description 服务端启动程序
 */
public final class Server {

    public static void main(String[] args) throws Exception {
        // 创建两个EventLoopGroup对象 两个线程组的原因：
        // NIO系列之Reactro模型 https://my.oschina.net/u/1859679/blog/1844109

        // boss 线程组: 用于服务端接受客户端的连接。
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker 线程组： 用于进行客户端的SocketChannel的数据读写。
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap 服务端的配置
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // ioServerSocketChannel 的处理器 本示例打印服务端的每个事件
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 设置连入服务端的 Client 的 SocketChannel 的处理器
                    .childHandler(new ServerInitializer());

            // 绑定端口
            ChannelFuture f = b.bind(8888);

            // 改过程就是启动服务端
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
