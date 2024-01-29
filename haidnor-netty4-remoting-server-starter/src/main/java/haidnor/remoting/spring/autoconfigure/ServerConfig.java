package haidnor.remoting.spring.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerConfig {

    @Value("${haidnor.netty4.remoting.server.listenPort:8080}")
    private int listenPort = 8080;

    @Value("${haidnor.netty4.remoting.server.serverWorkerThreads:8}")
    private int serverWorkerThreads = 8;

    @Value("${haidnor.netty4.remoting.server.serverCallbackExecutorThreads:0}")
    private int serverCallbackExecutorThreads = 0;

    @Value("${haidnor.netty4.remoting.server.serverSelectorThreads:3}")
    private int serverSelectorThreads = 3;

    @Value("${haidnor.netty4.remoting.server.serverOnewaySemaphoreValue:256}")
    private int serverOnewaySemaphoreValue = 256;

    @Value("${haidnor.netty4.remoting.server.serverAsyncSemaphoreValue:64}")
    private int serverAsyncSemaphoreValue = 64;

    @Value("${haidnor.netty4.remoting.server.timeoutMillis:5000}")
    private long timeoutMillis = 5000;

    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    @Value("${haidnor.netty4.remoting.server.serverChannelMaxReaderIdleTimeSeconds:0}")
    private int serverChannelMaxReaderIdleTimeSeconds = 0;

    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    @Value("${haidnor.netty4.remoting.server.serverChannelMaxWriterIdleTimeSeconds:0}")
    private int serverChannelMaxWriterIdleTimeSeconds = 0;

    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    @Value("${haidnor.netty4.remoting.server.serverChannelMaxAllIdleTimeSeconds:0}")
    private int serverChannelMaxAllIdleTimeSeconds = 0;

    @Value("${haidnor.netty4.remoting.server.serverSocketSndBufSize:65535}")
    private int serverSocketSndBufSize = 65535;

    @Value("${haidnor.netty4.remoting.server.serverSocketRcvBufSize:65535}")
    private int serverSocketRcvBufSize = 65535;

    @Value("${haidnor.netty4.remoting.server.frameMaxLength:16777216}")
    private int frameMaxLength = 16777216;

    @Value("${haidnor.netty4.remoting.server.serverPooledByteBufAllocatorEnable:true}")
    private boolean serverPooledByteBufAllocatorEnable = true;

    /**
     * make make install
     * <p>
     * ../glibc-2.10.1/configure \ --prefix=/usr \ --with-headers=/usr/include \
     * --host=x86_64-linux-gnu \ --build=x86_64-pc-linux-gnu \ --without-gd
     */
    @Value("${haidnor.netty4.remoting.server.useEpollNativeSelector:false}")
    private boolean useEpollNativeSelector = false;

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getServerWorkerThreads() {
        return serverWorkerThreads;
    }

    public void setServerWorkerThreads(int serverWorkerThreads) {
        this.serverWorkerThreads = serverWorkerThreads;
    }

    public int getServerCallbackExecutorThreads() {
        return serverCallbackExecutorThreads;
    }

    public void setServerCallbackExecutorThreads(int serverCallbackExecutorThreads) {
        this.serverCallbackExecutorThreads = serverCallbackExecutorThreads;
    }

    public int getServerSelectorThreads() {
        return serverSelectorThreads;
    }

    public void setServerSelectorThreads(int serverSelectorThreads) {
        this.serverSelectorThreads = serverSelectorThreads;
    }

    public int getServerOnewaySemaphoreValue() {
        return serverOnewaySemaphoreValue;
    }

    public void setServerOnewaySemaphoreValue(int serverOnewaySemaphoreValue) {
        this.serverOnewaySemaphoreValue = serverOnewaySemaphoreValue;
    }

    public int getServerAsyncSemaphoreValue() {
        return serverAsyncSemaphoreValue;
    }

    public void setServerAsyncSemaphoreValue(int serverAsyncSemaphoreValue) {
        this.serverAsyncSemaphoreValue = serverAsyncSemaphoreValue;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public int getServerChannelMaxReaderIdleTimeSeconds() {
        return serverChannelMaxReaderIdleTimeSeconds;
    }

    public void setServerChannelMaxReaderIdleTimeSeconds(int serverChannelMaxReaderIdleTimeSeconds) {
        this.serverChannelMaxReaderIdleTimeSeconds = serverChannelMaxReaderIdleTimeSeconds;
    }

    public int getServerChannelMaxWriterIdleTimeSeconds() {
        return serverChannelMaxWriterIdleTimeSeconds;
    }

    public void setServerChannelMaxWriterIdleTimeSeconds(int serverChannelMaxWriterIdleTimeSeconds) {
        this.serverChannelMaxWriterIdleTimeSeconds = serverChannelMaxWriterIdleTimeSeconds;
    }

    public int getServerChannelMaxAllIdleTimeSeconds() {
        return serverChannelMaxAllIdleTimeSeconds;
    }

    public void setServerChannelMaxAllIdleTimeSeconds(int serverChannelMaxAllIdleTimeSeconds) {
        this.serverChannelMaxAllIdleTimeSeconds = serverChannelMaxAllIdleTimeSeconds;
    }

    public int getServerSocketSndBufSize() {
        return serverSocketSndBufSize;
    }

    public void setServerSocketSndBufSize(int serverSocketSndBufSize) {
        this.serverSocketSndBufSize = serverSocketSndBufSize;
    }

    public int getServerSocketRcvBufSize() {
        return serverSocketRcvBufSize;
    }

    public void setServerSocketRcvBufSize(int serverSocketRcvBufSize) {
        this.serverSocketRcvBufSize = serverSocketRcvBufSize;
    }

    public int getFrameMaxLength() {
        return frameMaxLength;
    }

    public void setFrameMaxLength(int frameMaxLength) {
        this.frameMaxLength = frameMaxLength;
    }

    public boolean isServerPooledByteBufAllocatorEnable() {
        return serverPooledByteBufAllocatorEnable;
    }

    public void setServerPooledByteBufAllocatorEnable(boolean serverPooledByteBufAllocatorEnable) {
        this.serverPooledByteBufAllocatorEnable = serverPooledByteBufAllocatorEnable;
    }

    public boolean isUseEpollNativeSelector() {
        return useEpollNativeSelector;
    }

    public void setUseEpollNativeSelector(boolean useEpollNativeSelector) {
        this.useEpollNativeSelector = useEpollNativeSelector;
    }
}
