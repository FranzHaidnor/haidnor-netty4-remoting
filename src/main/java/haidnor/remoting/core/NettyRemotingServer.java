package haidnor.remoting.core;

import haidnor.remoting.ChannelEventListener;
import haidnor.remoting.InvokeCallback;
import haidnor.remoting.RPCHook;
import haidnor.remoting.RemotingServer;
import haidnor.remoting.common.CommandRegistrar;
import haidnor.remoting.common.Pair;
import haidnor.remoting.common.TlsMode;
import haidnor.remoting.exception.RemotingSendRequestException;
import haidnor.remoting.exception.RemotingTimeoutException;
import haidnor.remoting.exception.RemotingTooMuchRequestException;
import haidnor.remoting.protocol.RemotingCommand;
import haidnor.remoting.util.RemotingHelper;
import haidnor.remoting.util.RemotingUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NettyRemotingServer extends NettyRemotingAbstract implements RemotingServer {
    private final CommandRegistrar commandRegistrar = new CommandRegistrar();

    private final NettyServerConfig serverConfig;
    private static final String HANDSHAKE_HANDLER_NAME = "handshakeHandler";
    private static final String TLS_HANDLER_NAME = "sslHandler";
    private static final String FILE_REGION_ENCODER_NAME = "fileRegionEncoder";
    private final ServerBootstrap serverBootstrap;
    private final EventLoopGroup eventLoopGroupSelector;
    private final EventLoopGroup eventLoopGroupBoss;
    private final ExecutorService publicExecutor;
    private ChannelEventListener channelEventListener;
    private final Timer timer = new Timer("ServerHouseKeepingService", true);
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    private int port = 0;

    // ChannelHandler --------------------------------------------------------------------------------------------------
    private final HandshakeHandler handshakeHandler = new HandshakeHandler(TlsSystemConfig.tlsMode);
    private final NettyEncoder nettyEncoder = new NettyEncoder();
    private final NettyConnectManageHandler connectionManageHandler = new NettyConnectManageHandler();
    private final NettyServerHandler serverHandler = new NettyServerHandler();

    public <T extends Enum<T>> NettyRemotingServer(final NettyServerConfig serverConfig, Class<T> command) {
        super(serverConfig.getServerOnewaySemaphoreValue(), serverConfig.getServerAsyncSemaphoreValue());

        // 注册接口
        Field[] fields = command.getFields();
        for (Field field : fields) {
            commandRegistrar.register(field.getName());
        }

        this.serverBootstrap = new ServerBootstrap();
        this.serverConfig = serverConfig;

        int publicThreadNums = serverConfig.getServerCallbackExecutorThreads();
        if (publicThreadNums <= 0) {
            publicThreadNums = 4;
        }

        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {
            private final AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "NettyServerPublicExecutor_" + this.threadIndex.incrementAndGet());
            }
        });

        if (useEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(1, new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyEPOLLBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });

            this.eventLoopGroupSelector = new EpollEventLoopGroup(serverConfig.getServerSelectorThreads(), new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);
                private final int threadTotal = serverConfig.getServerSelectorThreads();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerEPOLLSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
                }
            });
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(1, new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyNIOBoss_%d", this.threadIndex.incrementAndGet()));
                }
            });

            this.eventLoopGroupSelector = new NioEventLoopGroup(serverConfig.getServerSelectorThreads(), new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);
                private final int threadTotal = serverConfig.getServerSelectorThreads();

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("NettyServerNIOSelector_%d_%d", threadTotal, this.threadIndex.incrementAndGet()));
                }
            });
        }

        loadSslContext();
    }

    public void loadSslContext() {
        TlsMode tlsMode = TlsSystemConfig.tlsMode;
        log.debug("Server is running in TLS {} mode", tlsMode.getName());

        if (tlsMode != TlsMode.DISABLED) {
            try {
                sslContext = TlsHelper.buildSslContext(false);
                log.debug("SSLContext created for server");
            } catch (CertificateException | IOException e) {
                log.debug("Failed to create SSLContext for server", e);
            }
        }
    }

    private boolean useEpoll() {
        return RemotingUtil.isLinuxPlatform() && serverConfig.isUseEpollNativeSelector() && Epoll.isAvailable();
    }

    @Override
    public void start() {
        int serverWorkerThreads = serverConfig.getServerWorkerThreads();
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "NettyServerCodecThread_" + this.threadIndex.incrementAndGet());
            }
        };
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(serverWorkerThreads, threadFactory);

        this.serverBootstrap
                .group(this.eventLoopGroupBoss, this.eventLoopGroupSelector)
                .channel(useEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, serverConfig.getServerSocketSndBufSize())
                .childOption(ChannelOption.SO_RCVBUF, serverConfig.getServerSocketRcvBufSize())
                .localAddress(new InetSocketAddress(this.serverConfig.getListenPort()))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                // InboundHandler
                                .addLast(defaultEventExecutorGroup, HANDSHAKE_HANDLER_NAME, handshakeHandler)
                                // DuplexHandler 连接空闲检测处理器 120s 没有读写将会被触发
                                .addLast(defaultEventExecutorGroup, new IdleStateHandler(serverConfig.getServerChannelMaxReaderIdleTimeSeconds(), serverConfig.getServerChannelMaxWriterIdleTimeSeconds(), serverConfig.getServerChannelMaxAllIdleTimeSeconds()))
                                .addLast(defaultEventExecutorGroup, connectionManageHandler)
                                // OutboundHandler 编码器
                                .addLast(defaultEventExecutorGroup, nettyEncoder)
                                // InboundHandler 解码器 ByteBuf > RemotingCommand
                                .addLast(defaultEventExecutorGroup, new NettyDecoder())
                                // InboundHandler
                                .addLast(defaultEventExecutorGroup, serverHandler);
                    }
                });


        if (serverConfig.isServerPooledByteBufAllocatorEnable()) {
            this.serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        }
        try {
            ChannelFuture sync = this.serverBootstrap.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
            this.port = addr.getPort();
            log.info("netty server start. port:" + this.port);
        } catch (InterruptedException e1) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e1);
        }

        if (this.channelEventListener != null) {
            this.nettyEventExecutor.start();
        }

        // 每秒清理过期的异步请求暂存结果
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    NettyRemotingServer.this.scanResponseTable();
                } catch (Throwable e) {
                    log.error("scanResponseTable exception", e);
                }
            }
        }, 1000 * 3, 1000);
    }

    @Override
    public void shutdown() {
        try {
            this.timer.cancel();
            this.eventLoopGroupBoss.shutdownGracefully();
            this.eventLoopGroupSelector.shutdownGracefully();
            this.nettyEventExecutor.shutdown();

            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error("NettyRemotingServer shutdown exception, ", e);
        }
        if (this.publicExecutor != null) {
            try {
                this.publicExecutor.shutdown();
            } catch (Exception e) {
                log.error("NettyRemotingServer shutdown exception, ", e);
            }
        }
    }

    @Override
    public void registerChannelEventListener(ChannelEventListener channelEventListener) {
        this.channelEventListener = channelEventListener;
    }

    @Override
    public void registerRPCHook(RPCHook rpcHook) {
        if (rpcHook != null && !rpcHooks.contains(rpcHook)) {
            rpcHooks.add(rpcHook);
        }
    }

    @Override
    public <T extends Enum<T>> void registerProcessor(T command, NettyRequestProcessor processor, ExecutorService executor) {
        ExecutorService executorThis = executor;
        if (null == executor) {
            executorThis = this.publicExecutor;
        }

        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<>(processor, executorThis);
        this.processorTable.put(command.name().hashCode(), pair);
    }

    @Override
    public void registerDefaultProcessor(NettyRequestProcessor processor, ExecutorService executor) {
        this.defaultRequestProcessor = new Pair<>(processor, executor);
    }

    @Override
    public int localListenPort() {
        return this.port;
    }

    @Override
    public Pair<NettyRequestProcessor, ExecutorService> getProcessorPair(int requestCode) {
        return processorTable.get(requestCode);
    }

    @Override
    public RemotingCommand invokeSync(final Channel channel, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingSendRequestException, RemotingTimeoutException {
        return this.invokeSyncImpl(channel, request, timeoutMillis);
    }

    @Override
    public void invokeAsync(Channel channel, RemotingCommand request, long timeoutMillis, InvokeCallback invokeCallback)
            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {
        this.invokeAsyncImpl(channel, request, timeoutMillis, invokeCallback);
    }

    @Override
    public void invokeOneway(Channel channel, RemotingCommand request, long timeoutMillis) throws InterruptedException,
            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {
        this.invokeOnewayImpl(channel, request, timeoutMillis);
    }

    @Override
    public ChannelEventListener getChannelEventListener() {
        return channelEventListener;
    }


    @Override
    public ExecutorService getCallbackExecutor() {
        return this.publicExecutor;
    }

    @ChannelHandler.Sharable
    class HandshakeHandler extends SimpleChannelInboundHandler<ByteBuf> {

        private static final byte HANDSHAKE_MAGIC_CODE = 0x16;
        private final TlsMode tlsMode;

        HandshakeHandler(TlsMode tlsMode) {
            this.tlsMode = tlsMode;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            // mark the current position so that we can peek the first byte to determine if the content is starting with TLS handshake
            msg.markReaderIndex();

            byte b = msg.getByte(0);
            if (b == HANDSHAKE_MAGIC_CODE) {  // 0x16
                switch (tlsMode) {
                    case DISABLED:
                        ctx.close();
                        log.warn("Clients intend to establish an SSL connection while this server is running in SSL disabled mode");
                        break;
                    case PERMISSIVE:
                    case ENFORCING:
                        if (null != sslContext) {
                            ctx.pipeline()
                                    .addAfter(defaultEventExecutorGroup, HANDSHAKE_HANDLER_NAME, TLS_HANDLER_NAME, sslContext.newHandler(ctx.channel().alloc()))
                                    .addAfter(defaultEventExecutorGroup, TLS_HANDLER_NAME, FILE_REGION_ENCODER_NAME, new FileRegionEncoder());
                            log.debug("Handlers prepended to channel pipeline to establish SSL connection");
                        } else {
                            ctx.close();
                            log.error("Trying to establish an SSL connection but sslContext is null");
                        }
                        break;
                    default:
                        log.warn("Unknown TLS mode");
                }
            } else if (tlsMode == TlsMode.ENFORCING) {
                ctx.close();
                log.warn("Clients intend to establish an insecure connection while this server is running in SSL enforcing mode");
            }
            // reset the reader index so that handshake negotiation may proceed as normal.
            msg.resetReaderIndex();

            try {
                // Remove this handler
                ctx.pipeline().remove(this);
            } catch (NoSuchElementException e) {
                log.error("Error while removing HandshakeHandler", e);
            }
            // Hand over this message to the next .
            ctx.fireChannelRead(msg.retain());
        }
    }

    @ChannelHandler.Sharable
    class NettyServerHandler extends SimpleChannelInboundHandler<RemotingCommand> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
            processMessageReceived(ctx, msg);
        }
    }

    @ChannelHandler.Sharable
    class NettyConnectManageHandler extends ChannelDuplexHandler {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPELINE: channelRegistered {}", remoteAddress);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPELINE: channelUnregistered, the channel[{}]", remoteAddress);
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPELINE: channelActive, the channel[{}]", remoteAddress);
            super.channelActive(ctx);

            if (NettyRemotingServer.this.channelEventListener != null) {
                NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, remoteAddress, ctx.channel()));
            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPELINE: channelInactive, the channel[{}]", remoteAddress);
            super.channelInactive(ctx);

            if (NettyRemotingServer.this.channelEventListener != null) {
                NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress, ctx.channel()));
            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.READER_IDLE)) {
                    final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    log.warn("NETTY SERVER PIPELINE: READER_IDLE [{}]", remoteAddress);
                    if (NettyRemotingServer.this.channelEventListener != null) {
                        NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.READER_IDLE, remoteAddress, ctx.channel()));
                    }
                } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                    final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    log.debug("NETTY SERVER PIPELINE: WRITER_IDLE [{}]", remoteAddress);
                    if (NettyRemotingServer.this.channelEventListener != null) {
                        NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.WRITER_IDLE, remoteAddress, ctx.channel()));
                    }
                } else if (event.state().equals(IdleState.ALL_IDLE)) {
                    final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
                    log.debug("NETTY SERVER PIPELINE: IDLE [{}]", remoteAddress);
                    if (NettyRemotingServer.this.channelEventListener != null) {
                        NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.ALL_IDLE, remoteAddress, ctx.channel()));
                    }
                }
            }
            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            log.debug("NETTY SERVER PIPELINE: exceptionCaught {}", remoteAddress);
            log.debug("NETTY SERVER PIPELINE: exceptionCaught exception.", cause);
            if (NettyRemotingServer.this.channelEventListener != null) {
                NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.EXCEPTION, remoteAddress, ctx.channel()));
            }
            RemotingUtil.closeChannel(ctx.channel());
        }
    }

}
