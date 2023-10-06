package com.kkuil.blackchat.core.websocket;

import com.kkuil.blackchat.core.websocket.handlers.NettyHttpHeadersHandler;
import com.kkuil.blackchat.core.websocket.handlers.NettyHttpParamsCollectorHandler;
import com.kkuil.blackchat.core.websocket.handlers.NettyWebSocketServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * @Author Kkuil
 * @Date 2023/09/21 11:00
 * @Description Netty Websocket的服务器启动类
 */
@Slf4j
@Configuration
public class NettyWebSocketServer {

    /**
     * websocket端口
     */
    @Value("${netty.websocket.port}")
    public int WEBSOCKET_PORT;

    /**
     * 读空闲时间
     */
    @Value("${netty.websocket.readIdleTimeSeconds}")
    public int READ_IDLE_TIME_SECONDS;

    /**
     * 写空闲时间
     */
    @Value("${netty.websocket.writeIdleTimeSeconds}")
    public int WRITE_IDLE_TIME_SECONDS;

    /**
     * 读写空闲时间
     */
    @Value("${netty.websocket.allIdleTimeSeconds}")
    public int ALL_IDLE_TIME_SECONDS;

    /**
     * websocket访问根路径
     */
    @Value("${netty.websocket.rootPath}")
    public String ROOT_PATH;

    /**
     * 非阻塞式线程执行器
     */
    private static final EventLoopGroup BOSS_GROUP = new NioEventLoopGroup(1);

    /**
     * 非阻塞式线程工作组
     */
    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup(NettyRuntime.availableProcessors());

    /**
     * 启动 ws server
     *
     * @throws InterruptedException 打断异常
     */
    @PostConstruct
    public void start() throws InterruptedException {
        log.info("websocket启动成功！");
        run();
    }

    /**
     * 销毁（优雅停机）
     */
    @PreDestroy
    public void destroy() {
        Future<?> future = BOSS_GROUP.shutdownGracefully();
        Future<?> future1 = WORKER_GROUP.shutdownGracefully();
        future.syncUninterruptibly();
        future1.syncUninterruptibly();
        log.info("websocket 已停止服务！");
    }

    /**
     * ws 运行方法
     *
     * @throws InterruptedException 打断异常
     */
    public void run() throws InterruptedException {
        // 服务器启动引导对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(BOSS_GROUP, WORKER_GROUP)
                // 设置非阻塞的socket服务
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 为 BOSS_GROUP 添加日志处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                // 设置】子处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 30秒客户端没有向服务器发送心跳则关闭连接
                        pipeline.addLast(new IdleStateHandler(READ_IDLE_TIME_SECONDS, WRITE_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS));
                        // 因为使用http协议，所以需要使用http的编码器，解码器
                        pipeline.addLast(new HttpServerCodec());
                        // 以块方式写，添加 chunkedWriter 处理器
                        pipeline.addLast(new ChunkedWriteHandler());
                        /**
                         * 说明：
                         *  1. http数据在传输过程中是分段的，HttpObjectAggregator可以把多个段聚合起来；
                         *  2. 这就是为什么当浏览器发送大量数据时，就会发出多次 http请求的原因
                         */
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        // 保存请求头（在Http升级Websocket之前进行请求头参数的收集）
                        pipeline.addLast(new NettyHttpHeadersHandler());
                        pipeline.addLast(new NettyHttpParamsCollectorHandler());
                        /**
                         * 说明：
                         *  1. 对于 WebSocket，它的数据是以帧frame 的形式传递的；
                         *  2. 可以看到 WebSocketFrame 下面有6个子类
                         *  3. 浏览器发送请求时： ws://localhost:7000/hello 表示请求的uri
                         *  4. WebSocketServerProtocolHandler 核心功能是把 http协议升级为 ws 协议，保持长连接；
                         *      是通过一个状态码 101 来切换的
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler(ROOT_PATH));
                        // 自定义handler ，处理业务逻辑
                        pipeline.addLast(new NettyWebSocketServerHandler());
                    }
                });
        // 启动服务器，监听端口，阻塞直到启动成功
        serverBootstrap.bind(WEBSOCKET_PORT).sync();
    }

}
