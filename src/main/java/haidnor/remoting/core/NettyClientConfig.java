package haidnor.remoting.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NettyClientConfig {

    private final String serverAddress;

    /**
     * Worker thread number
     */
    private int clientWorkerThreads = NettySystemConfig.clientWorkerSize;
    private int clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();
    private int clientOnewaySemaphoreValue = NettySystemConfig.CLIENT_ONEWAY_SEMAPHORE_VALUE;
    private int clientAsyncSemaphoreValue = NettySystemConfig.CLIENT_ASYNC_SEMAPHORE_VALUE;
    private int connectTimeoutMillis = NettySystemConfig.connectTimeoutMillis;
    private long channelNotActiveInterval = 1000 * 60;
    private long timeoutMillis = 5000;
    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    private int clientChannelMaxIdleTimeSeconds = NettySystemConfig.clientChannelMaxIdleTimeSeconds;

    private int clientSocketSndBufSize = NettySystemConfig.socketSndbufSize;
    private int clientSocketRcvBufSize = NettySystemConfig.socketRcvbufSize;
    private boolean clientPooledByteBufAllocatorEnable = false;
    private boolean clientCloseSocketIfTimeout = NettySystemConfig.clientCloseSocketIfTimeout;

    private boolean useTLS;

    public NettyClientConfig(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
