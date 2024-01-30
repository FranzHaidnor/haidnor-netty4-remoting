package haidnor.remoting.client.spring.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientConfig {

    @Value("${haidnor.netty4.remoting.client.processorThreads:2}")
    private int processorThreads = 2;

    /**
     * Worker thread number
     */
    @Value("${haidnor.netty4.remoting.client.clientWorkerThreads:4}")
    private int clientWorkerThreads = 4;

    @Value("${haidnor.netty4.remoting.client.clientCallbackExecutorThreads:4}")
    private int clientCallbackExecutorThreads;

    @Value("${haidnor.netty4.remoting.client.clientOnewaySemaphoreValue:65536}")
    private int clientOnewaySemaphoreValue = 65536;

    @Value("${haidnor.netty4.remoting.client.clientAsyncSemaphoreValue:65536}")
    private int clientAsyncSemaphoreValue = 65536;

    @Value("${haidnor.netty4.remoting.client.connectTimeoutMillis:3000}")
    private int connectTimeoutMillis = 3000;

    @Value("${haidnor.netty4.remoting.client.channelNotActiveInterval:60000}")
    private long channelNotActiveInterval = 1000 * 60;

    @Value("${haidnor.netty4.remoting.client.timeoutMillis:5000}")
    private long timeoutMillis = 5000;

    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    @Value("${haidnor.netty4.remoting.client.clientChannelMaxReaderIdleTimeSeconds:0}")
    private int clientChannelMaxReaderIdleTimeSeconds = 0;

    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    @Value("${haidnor.netty4.remoting.client.clientChannelMaxWriterIdleTimeSeconds:0}")
    private int clientChannelMaxWriterIdleTimeSeconds = 0;

    /**
     * IdleStateEvent will be triggered when neither read nor write was performed for
     * the specified period of this time. Specify {@code 0} to disable
     */
    @Value("${haidnor.netty4.remoting.client.clientChannelMaxAllIdleTimeSeconds:0}")
    private int clientChannelMaxAllIdleTimeSeconds = 0;

    @Value("${haidnor.netty4.remoting.client.clientSocketSndBufSize:65536}")
    private int clientSocketSndBufSize = 65536;

    @Value("${haidnor.netty4.remoting.client.clientSocketRcvBufSize:65536}")
    private int clientSocketRcvBufSize = 65536;

    @Value("${haidnor.netty4.remoting.client.frameMaxLength:65536}")
    private int frameMaxLength = 65536;

    @Value("${haidnor.netty4.remoting.client.clientPooledByteBufAllocatorEnable:false}")
    private boolean clientPooledByteBufAllocatorEnable = false;

    @Value("${haidnor.netty4.remoting.client.clientCloseSocketIfTimeout:true}")
    private boolean clientCloseSocketIfTimeout = true;

    @Value("${haidnor.netty4.remoting.client.useTLS:false}")
    private boolean useTLS = false;

    public int getProcessorThreads() {
        return processorThreads;
    }

    public void setProcessorThreads(int processorThreads) {
        this.processorThreads = processorThreads;
    }

    public int getClientWorkerThreads() {
        return clientWorkerThreads;
    }

    public void setClientWorkerThreads(int clientWorkerThreads) {
        this.clientWorkerThreads = clientWorkerThreads;
    }

    public int getClientCallbackExecutorThreads() {
        return clientCallbackExecutorThreads;
    }

    public void setClientCallbackExecutorThreads(int clientCallbackExecutorThreads) {
        this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
    }

    public int getClientOnewaySemaphoreValue() {
        return clientOnewaySemaphoreValue;
    }

    public void setClientOnewaySemaphoreValue(int clientOnewaySemaphoreValue) {
        this.clientOnewaySemaphoreValue = clientOnewaySemaphoreValue;
    }

    public int getClientAsyncSemaphoreValue() {
        return clientAsyncSemaphoreValue;
    }

    public void setClientAsyncSemaphoreValue(int clientAsyncSemaphoreValue) {
        this.clientAsyncSemaphoreValue = clientAsyncSemaphoreValue;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public long getChannelNotActiveInterval() {
        return channelNotActiveInterval;
    }

    public void setChannelNotActiveInterval(long channelNotActiveInterval) {
        this.channelNotActiveInterval = channelNotActiveInterval;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public int getClientChannelMaxReaderIdleTimeSeconds() {
        return clientChannelMaxReaderIdleTimeSeconds;
    }

    public void setClientChannelMaxReaderIdleTimeSeconds(int clientChannelMaxReaderIdleTimeSeconds) {
        this.clientChannelMaxReaderIdleTimeSeconds = clientChannelMaxReaderIdleTimeSeconds;
    }

    public int getClientChannelMaxWriterIdleTimeSeconds() {
        return clientChannelMaxWriterIdleTimeSeconds;
    }

    public void setClientChannelMaxWriterIdleTimeSeconds(int clientChannelMaxWriterIdleTimeSeconds) {
        this.clientChannelMaxWriterIdleTimeSeconds = clientChannelMaxWriterIdleTimeSeconds;
    }

    public int getClientChannelMaxAllIdleTimeSeconds() {
        return clientChannelMaxAllIdleTimeSeconds;
    }

    public void setClientChannelMaxAllIdleTimeSeconds(int clientChannelMaxAllIdleTimeSeconds) {
        this.clientChannelMaxAllIdleTimeSeconds = clientChannelMaxAllIdleTimeSeconds;
    }

    public int getClientSocketSndBufSize() {
        return clientSocketSndBufSize;
    }

    public void setClientSocketSndBufSize(int clientSocketSndBufSize) {
        this.clientSocketSndBufSize = clientSocketSndBufSize;
    }

    public int getClientSocketRcvBufSize() {
        return clientSocketRcvBufSize;
    }

    public void setClientSocketRcvBufSize(int clientSocketRcvBufSize) {
        this.clientSocketRcvBufSize = clientSocketRcvBufSize;
    }

    public int getFrameMaxLength() {
        return frameMaxLength;
    }

    public void setFrameMaxLength(int frameMaxLength) {
        this.frameMaxLength = frameMaxLength;
    }

    public boolean isClientPooledByteBufAllocatorEnable() {
        return clientPooledByteBufAllocatorEnable;
    }

    public void setClientPooledByteBufAllocatorEnable(boolean clientPooledByteBufAllocatorEnable) {
        this.clientPooledByteBufAllocatorEnable = clientPooledByteBufAllocatorEnable;
    }

    public boolean isClientCloseSocketIfTimeout() {
        return clientCloseSocketIfTimeout;
    }

    public void setClientCloseSocketIfTimeout(boolean clientCloseSocketIfTimeout) {
        this.clientCloseSocketIfTimeout = clientCloseSocketIfTimeout;
    }

    public boolean isUseTLS() {
        return useTLS;
    }

    public void setUseTLS(boolean useTLS) {
        this.useTLS = useTLS;
    }
}
