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
    private int clientWorkerThreads = 4;
    private int clientCallbackExecutorThreads = Runtime.getRuntime().availableProcessors();
    private int clientOnewaySemaphoreValue = 65535;
    private int clientAsyncSemaphoreValue = 65535;
    private int connectTimeoutMillis = 3000;
    private long channelNotActiveInterval = 1000 * 60;
    private long timeoutMillis = 5000;
    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    private int clientChannelMaxIdleTimeSeconds = 60;
    private int clientSocketSndBufSize = 65535;
    private int clientSocketRcvBufSize = 65535;
    private boolean clientPooledByteBufAllocatorEnable = false;
    private boolean clientCloseSocketIfTimeout = true;
    private boolean useTLS;

    public NettyClientConfig(String serverAddress) {
        this.serverAddress = serverAddress;
    }

}
